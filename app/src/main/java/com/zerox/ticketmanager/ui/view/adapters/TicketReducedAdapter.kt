package com.zerox.ticketmanager.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity

class TicketReducedAdapter(private val tickets: List<TicketEntity>):RecyclerView.Adapter<TicketReducedViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketReducedViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TicketReducedViewHolder(layoutInflater.inflate(R.layout.item_ticket_reduced, parent, false))
    }

    override fun onBindViewHolder(holder: TicketReducedViewHolder, position: Int) {
        val item = tickets[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = tickets.size
}