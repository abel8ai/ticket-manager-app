package com.zerox.ticketmanager.ui.view.adapters

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.ItemTicketBinding
import com.zerox.ticketmanager.ui.view.WorkTicketActivity

class TicketViewHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding = ItemTicketBinding.bind(view)
    fun bind(ticket: TicketEntity){
        binding.tvClientName.text = ticket.clientName
        binding.btnViewTicket.setOnClickListener {
            val intent = Intent(binding.root.context,WorkTicketActivity::class.java)
            intent.putExtra("ticket_id",ticket.id)
            binding.root.context.startActivity(intent)
        }
    }
}