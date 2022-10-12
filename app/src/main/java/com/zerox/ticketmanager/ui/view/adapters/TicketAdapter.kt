package com.zerox.ticketmanager.ui.view.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
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
            CoroutineScope(Dispatchers.IO).launch {
                dashboardViewModel
            }

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


}