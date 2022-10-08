package com.zerox.ticketmanager.ui.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.widget.EditText
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
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


@AndroidEntryPoint
class DashBoardActivity : AppCompatActivity() {
    // viewBinding
    private lateinit var binding: ActivityDashboardBinding
    // inject the dashboard viewmodel into the activity
    private val dashboardViewModel: DashboardViewModel by viewModels()
    private var ticketList = emptyList<TicketEntity>()
    private var isRotated = false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // configure the support action bar's title
        supportActionBar!!.title = resources.getString(R.string.dashboard_title)

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

        // on click listener for add ticket button
        binding.fabAddTicket.setOnClickListener {
            val intent = Intent(this,WorkTicketActivity::class.java)
            startActivity(intent)
            //showAddTicketDialog()
        }

        // observer to receive tickets data when ready
        dashboardViewModel.ticketModel.observe(this) {
            ticketList = it
            initRecyclerView()
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
                val ticket = TicketEntity(null, mClientName, mAddress, mDate)
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
}