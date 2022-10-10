package com.zerox.ticketmanager.ui.view

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import android.widget.SearchView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.gms.maps.model.PolylineOptions
import com.google.android.libraries.places.api.Places
import com.google.android.libraries.places.api.net.PlacesClient
import com.google.maps.DirectionsApi
import com.google.maps.GeoApiContext
import com.google.maps.android.PolyUtil
import com.google.maps.errors.NotFoundException
import com.google.maps.model.DirectionsResult
import com.google.maps.model.TravelMode
import com.zerox.ticketmanager.BuildConfig
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.databinding.ActivityDirectionsBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.joda.time.DateTime
import java.util.concurrent.TimeUnit


class DirectionsActivity : AppCompatActivity(), OnMapReadyCallback {

    // viewBinding
    private lateinit var binding: ActivityDirectionsBinding

    // google maps variables
    private var lastKnownLocation: Location? = null
    private val defaultLocation = LatLng(-33.8523341, 151.2106085)
    private var locationPermissionGranted = false
    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var placesClient: PlacesClient
    private var map: GoogleMap? = null
    private var cameraPosition: CameraPosition? = null

    // variable to retrieve direction from extras
    private var destination: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDirectionsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // set title and back function for the support action bar
        supportActionBar!!.title = resources.getString(R.string.directions_title)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        // retrieve direction if it comes from work ticket screen
        destination = intent.extras?.getString("direction")

        // set the direction in searchview if it comes from work ticket screen
        if (destination != null) {
            binding.svDirection.setQuery(destination, false)
        }

        // listener to update route when the user submits new direction
        binding.svDirection.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                if (query != null) {
                    destination = query
                    CoroutineScope(Dispatchers.IO).launch {
                        try {
                            val result = getRoute()
                            runOnUiThread {
                                // adds gps markers in the map
                                addMarkersToMap(result, map!!)
                                // draws the route line
                                addPolyline(result, map!!)
                            }
                        } catch (exception: NotFoundException) {
                            runOnUiThread {
                                Toast.makeText(
                                    this@DirectionsActivity,
                                    "Unable to find destination",
                                    Toast.LENGTH_SHORT
                                ).show()
                            }
                        }
                    }
                }
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION)
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION)
        }
        // Construct a PlacesClient
        Places.initialize(applicationContext, BuildConfig.MAPS_API_KEY)
        placesClient = Places.createClient(this)

        // Construct a FusedLocationProviderClient.
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.frag_map) as SupportMapFragment?
        mapFragment?.getMapAsync(this)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        map?.let { map ->
            outState.putParcelable(KEY_CAMERA_POSITION, map.cameraPosition)
            outState.putParcelable(KEY_LOCATION, lastKnownLocation)
        }
        super.onSaveInstanceState(outState)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return super.onSupportNavigateUp()
    }

    override fun onMapReady(map: GoogleMap) {
        this.map = map
        // Prompt the user for permission.
        getLocationPermission()
    }

    private fun getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(
                this.applicationContext,
                Manifest.permission.ACCESS_FINE_LOCATION
            )
            == PackageManager.PERMISSION_GRANTED
        ) {
            locationPermissionGranted = true
            // Turn on the My Location layer and the related control on the map.
            updateLocationUI()

            // Get the current location of the device and set the position of the map.
            getDeviceLocation()
        } else {
            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        }
    }

    @SuppressLint("MissingPermission")
    private fun getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                val locationResult = fusedLocationProviderClient.lastLocation
                locationResult.addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Set the map's camera position to the current location of the device.
                        lastKnownLocation = task.result
                        if (lastKnownLocation != null) {
                            map?.moveCamera(
                                CameraUpdateFactory.newLatLngZoom(
                                    LatLng(
                                        lastKnownLocation!!.latitude,
                                        lastKnownLocation!!.longitude
                                    ), DEFAULT_ZOOM.toFloat()
                                )
                            )
                            // get directions to destination from Directions API
                            if (destination != null)
                                CoroutineScope(Dispatchers.IO).launch {
                                    try {
                                        val result = getRoute()
                                        runOnUiThread {
                                            // adds gps markers in the map
                                            addMarkersToMap(result, map!!)
                                            // draws the route line
                                            addPolyline(result, map!!)
                                        }
                                    } catch (exception: Exception) {
                                        runOnUiThread {
                                            Toast.makeText(
                                                this@DirectionsActivity,
                                                exception.message,
                                                Toast.LENGTH_SHORT
                                            ).show()
                                        }
                                    }
                                }
                        }
                    } else {
                        Log.d(TAG, "Current location is null. Using defaults.")
                        Log.e(TAG, "Exception: %s", task.exception)
                        map?.moveCamera(
                            CameraUpdateFactory
                                .newLatLngZoom(defaultLocation, DEFAULT_ZOOM.toFloat())
                        )
                        map?.uiSettings?.isMyLocationButtonEnabled = false
                    }
                }
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        locationPermissionGranted = false
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {

                // If request is cancelled, the result arrays are empty.
                if (grantResults.isNotEmpty() &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED
                ) {
                    locationPermissionGranted = true
                    // Turn on the My Location layer and the related control on the map.
                    updateLocationUI()

                    // Get the current location of the device and set the position of the map.
                    getDeviceLocation()
                    // setUserLocation
                }
            }
            else -> super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }
    }

    /**
     * Updates the map's UI setti+ngs based on whether the user has granted location permission.
     */
    @SuppressLint("MissingPermission")
    private fun updateLocationUI() {
        if (map == null) {
            return
        }
        try {
            if (locationPermissionGranted) {
                map?.isMyLocationEnabled = true
                map?.uiSettings?.isMyLocationButtonEnabled = true
            } else {
                map?.isMyLocationEnabled = false
                map?.uiSettings?.isMyLocationButtonEnabled = false
                lastKnownLocation = null
                getLocationPermission()
            }
        } catch (e: SecurityException) {
            Log.e("Exception: %s", e.message, e)
        }
    }

    // establishes the conecction with the Directons API
    private fun getGeoContext(): GeoApiContext? {
        val geoApiContext = GeoApiContext()
        return geoApiContext.setQueryRateLimit(3)
            .setApiKey(BuildConfig.MAPS_API_KEY)
            .setConnectTimeout(1, TimeUnit.SECONDS)
            .setReadTimeout(1, TimeUnit.SECONDS)
            .setWriteTimeout(1, TimeUnit.SECONDS)
    }

    private fun getRoute(): DirectionsResult {
        val now = DateTime()
        val lat = lastKnownLocation!!.latitude.toString()
        val long = lastKnownLocation!!.longitude.toString()
        val origin = "$lat,$long"
        val route =
            DirectionsApi.newRequest(getGeoContext()).mode(TravelMode.DRIVING).origin(origin)
                .destination(destination).departureTime(now)
        return route.await()
    }

    private fun addMarkersToMap(results: DirectionsResult, mMap: GoogleMap) {
        mMap.clear()
        mMap.addMarker(
            MarkerOptions().position(
                LatLng(
                    results.routes[0].legs[0].endLocation.lat,
                    results.routes[0].legs[0].endLocation.lng
                )
            ).title(
                results.routes[0].legs[0].startAddress
            )
        )
    }

    private fun addPolyline(results: DirectionsResult, mMap: GoogleMap) {
        val decodedPath: List<LatLng> = PolyUtil.decode(
            results.routes[0].overviewPolyline.encodedPath
        )
        mMap.addPolyline(PolylineOptions().addAll(decodedPath))
    }

    companion object {
        private val TAG = DirectionsActivity::class.java.simpleName
        private const val DEFAULT_ZOOM = 15
        private const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1

        // Keys for storing activity state.
        private const val KEY_CAMERA_POSITION = "camera_position"
        private const val KEY_LOCATION = "location"
    }
}