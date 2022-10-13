package com.zerox.ticketmanager.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.DialogAddTicketBinding
import com.zerox.ticketmanager.ui.utils.DatePickerFragment
import com.zerox.ticketmanager.ui.utils.TimePickerFragment
import com.zerox.ticketmanager.ui.viewmodel.DashboardViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TicketAdapter(private val tickets: List<TicketEntity>,private val dashboardViewModel: DashboardViewModel,private val userId:Int, private val context: Context):RecyclerView.Adapter<TicketViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TicketViewHolder(layoutInflater.inflate(R.layout.item_ticket, parent, false))
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val item = tickets[position]
        holder.bind(item)
        holder.binding.cvElement.setOnClickListener{
            showModifyTicketDialog(item)
        }
        holder.binding.cvElement.setOnLongClickListener{
            val builder = AlertDialog.Builder(context)
            builder.setTitle(context.resources.getString(R.string.dialog_delete_ticket))
            builder.setMessage(context.resources.getString(R.string.delete_ticket_confirmation))
            builder.create()
            builder.setPositiveButton(context.resources.getString(R.string.accept)) { _, _ ->
                CoroutineScope(Dispatchers.IO).launch {
                dashboardViewModel.deleteTicket(item,userId)
            } }
            builder.setNegativeButton(context.resources.getString(R.string.cancel)) { dialog, _ ->
                dialog!!.dismiss() }
            builder.show()
            true
        }
    }

    override fun getItemCount(): Int = tickets.size

    private fun showModifyTicketDialog(ticket: TicketEntity) {
        // viewBinding for the add ticket dialog
        val dialogBinding: DialogAddTicketBinding = DialogAddTicketBinding.inflate((context as AppCompatActivity).layoutInflater)

        // shows DatePickerFragment to select date
        dialogBinding.etDate.setOnClickListener {
            showDatePicker(dialogBinding.etDate)
        }
        dialogBinding.etTime.setOnClickListener {
            showTimePicker(dialogBinding.etTime)
        }
        // building the Alert Dialog
        val builder = AlertDialog.Builder(context)

        // fill fields with previous data
        dialogBinding.etClientName.setText(ticket.clientName)
        dialogBinding.etMotive.setText(ticket.motive)
        dialogBinding.etDeptClass.setText(ticket.deptClass)
        dialogBinding.etServiceType.setText(ticket.serviceType)
        dialogBinding.etReasonCall.setText(ticket.reasonCall)
        dialogBinding.etNotes.setText(ticket.notes)
        dialogBinding.etAddress.setText(ticket.address)
        dialogBinding.etPhone.setText(ticket.phoneNumber)
        dialogBinding.etTime.setText(ticket.time)
        dialogBinding.etDate.setText(ticket.date)

        // change add button text
        dialogBinding.btnAdicionar.setText(R.string.modify)

        builder.setTitle(context.resources.getString(R.string.dialog_add_ticket_title))
        builder.setView(dialogBinding.root)
        builder.setCancelable(false)
        builder.create()
        val dialog = builder.show()
        dialogBinding.btnCancelar.setOnClickListener {
            dialog.dismiss()
        }
        dialogBinding.btnAdicionar.setOnClickListener {

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
                || serviceType.isEmpty() || time.isEmpty()
            ) {
                val hint = context.resources.getString(R.string.mandatory_field)
                if (dialogBinding.etClientName.text.isEmpty()) {
                    dialogBinding.etClientName.hint = hint
                    dialogBinding.etClientName.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etAddress.text.isEmpty()) {
                    dialogBinding.etAddress.hint = hint
                    dialogBinding.etAddress.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etDate.text.isEmpty()) {
                    dialogBinding.etDate.hint = hint
                    dialogBinding.etDate.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etPhone.text.isEmpty()) {
                    dialogBinding.etPhone.hint = hint
                    dialogBinding.etPhone.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etNotes.text.isEmpty()) {
                    dialogBinding.etNotes.hint = hint
                    dialogBinding.etNotes.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etMotive.text.isEmpty()) {
                    dialogBinding.etMotive.hint = hint
                    dialogBinding.etMotive.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etDeptClass.text.isEmpty()) {
                    dialogBinding.etDeptClass.hint = hint
                    dialogBinding.etDeptClass.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etServiceType.text.isEmpty()) {
                    dialogBinding.etServiceType.hint = hint
                    dialogBinding.etServiceType.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etTime.text.isEmpty()) {
                    dialogBinding.etTime.hint = hint
                    dialogBinding.etTime.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
                if (dialogBinding.etReasonCall.text.isEmpty()) {
                    dialogBinding.etReasonCall.hint = hint
                    dialogBinding.etReasonCall.setHintTextColor(context.resources.getColor(R.color.light_red))
                }
            } else {
                val modTicket = TicketEntity(
                    ticket.id, userId, motive, clientName, phone, deptClass, serviceType,
                    notes, reasonCall, address, time, date
                )

                CoroutineScope(Dispatchers.IO).launch {
                    dashboardViewModel.updateTicket(modTicket, userId)
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
                var finalMinutes = ""
                if (minutes < 10)
                    finalMinutes = "0$minutes"
                else
                    finalMinutes = minutes.toString()

                var selectedTime = ""
                if (hour < 12)
                    selectedTime = "$hour:$finalMinutes am"
                else
                    selectedTime = "${hour - 12}:$finalMinutes pm"
                etTime.text = selectedTime
            }
        newFragment.show((context as AppCompatActivity).supportFragmentManager, "datePicker")
    }

    // dialog to load DatePicker Fragment and select date when clicking on the time edittext
    private fun showDatePicker(etDate: TextView) {
        val newFragment: DatePickerFragment =
            DatePickerFragment.newInstance { datePicker, year, month, day ->
                val selectedDate = day.toString() + "/" + (month + 1) + "/" + year
                etDate.text = selectedDate
            }
        newFragment.show((context as AppCompatActivity).supportFragmentManager, "datePicker")
    }

}