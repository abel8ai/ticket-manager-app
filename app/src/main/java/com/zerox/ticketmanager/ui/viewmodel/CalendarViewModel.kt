package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import javax.inject.Inject

class CalendarViewModel @Inject constructor(private val repository: Repository):ViewModel(){
    private val _ticketsModel = MutableLiveData<MutableList<TicketEntity>>()
    val ticketsModel: LiveData<MutableList<TicketEntity>> get() = _ticketsModel

    suspend fun getAllTickets() {
        _ticketsModel.postValue(repository.getAllTickets())
    }

    suspend fun getTicketsByDate(day: CalendarDay){
        val date = "${day.year}/${day.month}/${day.day}"
        _ticketsModel.postValue(repository.getTicketsByDate(date))
    }
}