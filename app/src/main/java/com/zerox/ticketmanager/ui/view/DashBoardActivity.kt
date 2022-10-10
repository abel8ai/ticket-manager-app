package com.zerox.ticketmanager.ui.view

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity

import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.auth.GoogleAuthUtil

import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions

import com.google.android.gms.common.api.ApiException

import com.google.android.gms.tasks.Task
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential

import com.zerox.ticketmanager.BuildConfig
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.ActivityDashboardBinding
import com.zerox.ticketmanager.databinding.DialogAddTicketBinding
import com.zerox.ticketmanager.ui.utils.DatePickerFragment
import com.zerox.ticketmanager.ui.utils.ViewAnimation
import com.zerox.ticketmanager.ui.view.adapters.TicketAdapter
import com.zerox.ticketmanager.ui.viewmodel.DashboardViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException


@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    // viewBinding
    private lateinit var binding: ActivityDashboardBinding
    private val RC_SIGN_IN: Int = 44556
    private val RC_GET_AUTH_CODE: Int = 4455622
    private var writingPermissionGranted:Boolean = false
    private val REQUEST_WRITE_EXTERNAL_STORAGE = 334533
    private lateinit var credential: GoogleCredential
    // inject the dashboard viewmodel into the activity
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var ticketList = emptyList<TicketEntity>()
    private var isRotated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // configure the support action bar's title

        // observers to receive tickets data when ready
        dashboardViewModel.ticketsModel.observe(this) {
            ticketList = it
            // initialize recyclerview with all the tickets
            initRecyclerView()
        }
        dashboardViewModel.ticketModel.observe(this) {
            val intent = Intent(this@DashBoardActivity, WorkTicketActivity::class.java)
            intent.putExtra("ticket_id", it.id)
            startActivity(intent)
        }

        // initialization for the menu elements
        ViewAnimation.init(binding.fabWorkTicket)
        ViewAnimation.init(binding.fabGetDirections)

        //on click listener to display menu elements with animation
        binding.fabMenu.setOnClickListener {
            isRotated = ViewAnimation.rotateFab(it, !isRotated)
            if (isRotated) {
                ViewAnimation.showIn(binding.fabWorkTicket)
                ViewAnimation.showIn(binding.fabGetDirections)
            } else {
                ViewAnimation.showOut(binding.fabWorkTicket)
                ViewAnimation.showOut(binding.fabGetDirections)
            }
        }

        // on click listener for work ticket menu element
        binding.fabWorkTicket.setOnClickListener {
            CoroutineScope(Dispatchers.IO).launch {
                dashboardViewModel.getLastTicketCreated()
            }
        }
        // on click listener for get directions menu element
        binding.fabGetDirections.setOnClickListener {
            startActivity(Intent(this, DirectionsActivity::class.java))
        }
        // on click listener for add ticket button
        binding.fabAddTicket.setOnClickListener {
            showAddTicketDialog()
        }
        // on click listener to sync tickets into calendar
        binding.fabSyncCalendar.setOnClickListener {
            signIn()
        }
        // on click listener to show tickets into calendar
        binding.fabCalendar.setOnClickListener {
            startActivity(Intent(this,CalendarActivity::class.java))
        }

        // load tickets information from database
        loadData()
    }

    private fun showAddTicketDialog() {
        // viewBinding for the add ticket dialog
        val dialogBinding: DialogAddTicketBinding = DialogAddTicketBinding.inflate(layoutInflater)

        // shows DatePickerFragment to select date
        dialogBinding.etDate.setOnClickListener {
            showDatePicker(dialogBinding.etDate)
        }
        // building the Alert Dialog
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_add_ticket_title))
        builder.setView(dialogBinding.root)
        builder.setCancelable(false)
        builder.create()
        val dialog = builder.show()
        dialogBinding.btCancelar.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btAdicionar.setOnClickListener {

            // getting the values from the dialog's viewBinding
            val mClientName = dialogBinding.etClientName.text.toString()
            val mAddress = dialogBinding.etAddress.text.toString()
            val mDate = dialogBinding.etDate.text.toString()
            val phone = dialogBinding.etPhone.text.toString()

            // fields validation
            if (mClientName.isEmpty() || mAddress.isEmpty() || mDate.isEmpty()) {
                val hint = resources.getString(R.string.mandatory_field)
                if (dialogBinding.etClientName.text.isEmpty()) {
                    dialogBinding.etClientName.hint = hint
                    dialogBinding.etClientName.setHintTextColor(Color.RED)
                }
                if (dialogBinding.etAddress.text.isEmpty()) {
                    dialogBinding.etAddress.hint = hint
                    dialogBinding.etAddress.setHintTextColor(Color.RED)
                }
                if (dialogBinding.etDate.text.isEmpty()) {
                    dialogBinding.etDate.hint = hint
                    dialogBinding.etDate.setHintTextColor(Color.RED)
                }
            } else {
                // create ticket and store it in database
                val ticket = TicketEntity(null, mClientName, phone, mAddress, mDate)
                CoroutineScope(Dispatchers.IO).launch {
                    dashboardViewModel.addTicket(ticket)
                }
                dialog.dismiss()
            }
        }
    }

    private fun loadData() {
        CoroutineScope(Dispatchers.IO).launch {
            dashboardViewModel.getAllTickets()
        }
    }

    private fun initRecyclerView() {
        val adapter = TicketAdapter(ticketList)
        binding.rvTickets.layoutManager = LinearLayoutManager(this)
        binding.rvTickets.adapter = adapter
    }

    private fun showDatePicker(etDate: TextView) {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day ->
                val selectedDate: String = day.toString() + "/" + (month + 1) + "/" + year
                etDate.text = selectedDate
            }
        newFragment.show(supportFragmentManager, "datePicker")
    }

    private fun getSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(BuildConfig.CLIENT_ID)
                .requestServerAuthCode(BuildConfig.CLIENT_ID, true)
                .build())
    }
    private fun signIn() {
        val signInIntent = getSignInClient().signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val result = completedTask.getResult(ApiException::class.java)
            val scope = "oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile"
            var accessToken = ""
            CoroutineScope(Dispatchers.IO).launch {
                accessToken = GoogleAuthUtil.getToken(this@DashBoardActivity, result.account!!, scope)
            }

            val credential = GoogleCredential().setAccessToken(accessToken)
            CoroutineScope(Dispatchers.IO).launch {
                dashboardViewModel.createGoogleCalendarEvent(credential)
            }

        } catch (e: ApiException) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w(ContentValues.TAG, "signInResult:failed code=" + e.statusCode)
        }
    }

    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_exit_app))
        builder.setMessage("Are you sure you want to close the app?")
        builder.create()
        builder.setPositiveButton("Accept") { _, _ -> finish()}
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog!!.dismiss() }
        builder.show()
    }
}