package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.prolificinteractive.materialcalendarview.CalendarDay
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class CalendarViewModel @Inject constructor(private val repository: Repository):ViewModel(){
    private val _allTickets = MutableLiveData<MutableList<TicketEntity>>()
    val alltickets: LiveData<MutableList<TicketEntity>> get() = _allTickets

    private val _ticketsByDate = MutableLiveData<MutableList<TicketEntity>>()
    val ticketsByDate: LiveData<MutableList<TicketEntity>> get() = _ticketsByDate

    suspend fun getAllTickets() {
        _allTickets.postValue(repository.getAllTickets())
    }

    suspend fun getTicketsByDate(day: CalendarDay){
        val date = "${day.day}/${day.month}/${day.year}"
        _ticketsByDate.postValue(repository.getTicketsByDate(date))
    }
}