package com.zerox.ticketmanager.ui.view.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.zerox.ticketmanager.R
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity

class TicketAdapter(private val tickets: List<TicketEntity>):RecyclerView.Adapter<TicketViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        return TicketViewHolder(layoutInflater.inflate(R.layout.item_ticket, parent, false))
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val item = tickets[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int = tickets.size
}