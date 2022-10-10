package com.zerox.ticketmanager.ui.view.adapters

import android.content.Intent
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.databinding.ItemTicketBinding
import com.zerox.ticketmanager.ui.view.WorkTicketActivity

class TicketViewHolder(view:View):RecyclerView.ViewHolder(view){

    private val binding = ItemTicketBinding.bind(view)
    fun bind(ticket: TicketEntity){
        val ticketLabel = binding.root.context.resources.getString(R.string.ticket_no)
        val ticketNo = ticket.id
        val result = "$ticketLabel$ticketNo"
        binding.tvIdTicket.text = result
        binding.tvAddress.text = ticket.address
        binding.tvDate.text = ticket.date
        binding.tvMotive.text = ticket.motive
        binding.tvTime.text = ticket.time
        binding.btnViewTicket.setOnClickListener {
            val intent = Intent(binding.root.context,WorkTicketActivity::class.java)
            intent.putExtra("ticket_id",ticket.id)
            binding.root.context.startActivity(intent)
        }
    }
}