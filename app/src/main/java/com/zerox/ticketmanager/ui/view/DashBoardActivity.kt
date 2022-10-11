package com.zerox.ticketmanager.ui.view

import android.content.ContentValues
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.createSavedStateHandle

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
import com.zerox.ticketmanager.ui.utils.TimePickerFragment
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

    // request code to get logged google account
    private val RC_SIGN_IN: Int = 44556

    // inject the dashboard viewmodel into the activity
    private val dashboardViewModel: DashboardViewModel by viewModels()

    private var ticketList = emptyList<TicketEntity>()

    // for animation purposes, if is rotated means that the submenu buttons are showned
    private var isRotated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // observer to receive tickets data when ready
        dashboardViewModel.allTickets.observe(this) {
            ticketList = it
            // initialize recyclerview with all the tickets
            initRecyclerView()
        }

        //observer to get last ticket created and launch Work ticket screen with that ticket's id
        dashboardViewModel.ticket.observe(this) {
            val intent = Intent(this@DashBoardActivity, WorkTicketActivity::class.java)
            intent.putExtra("ticket_id", it.id)
            startActivity(intent)
        }


        // initialization for the menu elements animator
        ViewAnimation.init(binding.fabWorkTicket)
        ViewAnimation.init(binding.fabGetDirections)

        //on click listener to display menu elements with animation
        binding.fabMenu.setOnClickListener {
            isRotated = ViewAnimation.rotateFab(binding.fabMenuIcon, !isRotated)
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
            // running coroutine to get last ticket created
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
        // nor working due to inability to get a proper credential from Google SignIn
        binding.fabSyncCalendar.setOnClickListener {
            //signIn()
        }
        // on click listener to show tickets in calendar view
        binding.fabCalendar.setOnClickListener {
            startActivity(Intent(this, CalendarActivity::class.java))
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
        dialogBinding.etTime.setOnClickListener {
            showTimePicker(dialogBinding.etTime)
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
            val clientName = dialogBinding.etClientName.text.toString()
            val address = dialogBinding.etAddress.text.toString()
            val date = dialogBinding.etDate.text.toString()
            val phone = dialogBinding.etPhone.text.toString()
            val motive = dialogBinding.etMotive.text.toString()
            val deptClass = dialogBinding.etDeptClass.text.toString()
            val notes = dialogBinding.etNotes.text.toString()
            val reasonCall = dialogBinding.etReasonCall.text.toString()
            val serviceType = dialogBinding.etServiceType.text.toString()
            val time = dialogBinding.etTime.text.toString()

            // fields validation
            if (clientName.isEmpty() || address.isEmpty() || date.isEmpty() || phone.isEmpty()
                || motive.isEmpty() || deptClass.isEmpty() || notes.isEmpty() || reasonCall.isEmpty()
                || serviceType.isEmpty() || time.isEmpty()) {
                val hint = resources.getString(R.string.mandatory_field)
                if (dialogBinding.etClientName.text.isEmpty()) {
                    dialogBinding.etClientName.hint = hint
                    dialogBinding.etClientName.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etAddress.text.isEmpty()) {
                    dialogBinding.etAddress.hint = hint
                    dialogBinding.etAddress.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etDate.text.isEmpty()) {
                    dialogBinding.etDate.hint = hint
                    dialogBinding.etDate.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etPhone.text.isEmpty()) {
                    dialogBinding.etPhone.hint = hint
                    dialogBinding.etPhone.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etNotes.text.isEmpty()) {
                    dialogBinding.etNotes.hint = hint
                    dialogBinding.etNotes.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etMotive.text.isEmpty()) {
                    dialogBinding.etMotive.hint = hint
                    dialogBinding.etMotive.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etDeptClass.text.isEmpty()) {
                    dialogBinding.etDeptClass.hint = hint
                    dialogBinding.etDeptClass.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etServiceType.text.isEmpty()) {
                    dialogBinding.etServiceType.hint = hint
                    dialogBinding.etServiceType.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etTime.text.isEmpty()) {
                    dialogBinding.etTime.hint = hint
                    dialogBinding.etTime.setHintTextColor(resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etReasonCall.text.isEmpty()) {
                    dialogBinding.etReasonCall.hint = hint
                    dialogBinding.etReasonCall.setHintTextColor(resources.getColor(R.color.light_red))
                }
            } else {
                // create ticket and store it in database
                val ticket = TicketEntity(
                    null, motive, clientName, phone, deptClass, serviceType,
                    notes, reasonCall, address, time, date
                )
                CoroutineScope(Dispatchers.IO).launch {
                    dashboardViewModel.addTicket(ticket)
                }
                // close the dialog when finished
                dialog.dismiss()
            }
        }
    }

    // dialog to load TimePicker Fragment and select time when clicking on the time edittext
    private fun showTimePicker(etTime: TextView) {
        val newFragment: TimePickerFragment =
            TimePickerFragment.newInstance { timepicker, hour, minutes ->
                var selectedTime = ""
                if(hour < 12)
                    selectedTime = "$hour:$minutes am"
                else
                    selectedTime = "${hour-12}:$minutes pm"
                etTime.text = selectedTime
            }
        newFragment.show(supportFragmentManager, "datePicker")
    }
    // dialog to load DatePicker Fragment and select date when clicking on the time edittext
    private fun showDatePicker(etDate: TextView) {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day ->
                val selectedDate = day.toString() + "/" + (month + 1) + "/" + year
                etDate.text = selectedDate
            }
        newFragment.show(supportFragmentManager, "datePicker")
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

    // closing app confirmation dialog
    override fun onBackPressed() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(resources.getString(R.string.dialog_exit_app))
        builder.setMessage("Are you sure you want to close the app?")
        builder.create()
        builder.setPositiveButton("Accept") { _, _ -> finishAffinity() }
        builder.setNegativeButton("Cancel") { dialog, _ -> dialog!!.dismiss() }
        builder.show()
    }

    ////////////////////////////////////////////////////////////////////////////////////////////////////
    // from here on are the methods to get google account and try to sync events into Google Calendar///
    ////////////////////////////////////////////////////////////////////////////////////////////////////

    // create the google signInClient and request user information
    private fun getSignInClient(): GoogleSignInClient {
        return GoogleSignIn.getClient(
            this,
            GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken(BuildConfig.CLIENT_ID)
                .requestServerAuthCode(BuildConfig.CLIENT_ID, true)
                .build()
        )
    }

    // start the signIN activity
    private fun signIn() {
        val signInIntent = getSignInClient().signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    // get the sign in results
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

    //get the Google authenticated account and create Google Credential for the syncronization of events
    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val result = completedTask.getResult(ApiException::class.java)
            val scope =
                "oauth2:https://www.googleapis.com/auth/plus.me https://www.googleapis.com/auth/userinfo.profile"
            var accessToken = ""
            CoroutineScope(Dispatchers.IO).launch {
                accessToken =
                    GoogleAuthUtil.getToken(this@DashBoardActivity, result.account!!, scope)
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
}