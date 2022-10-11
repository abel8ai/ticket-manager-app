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
    // live data to get all the tickets and notify the view
    private val _allTickets = MutableLiveData<MutableList<TicketEntity>>()
    val alltickets: LiveData<MutableList<TicketEntity>> get() = _allTickets
    // live data to get the tickets of specific data and notify the view
    private val _ticketsByDate = MutableLiveData<MutableList<TicketEntity>>()
    val ticketsByDate: LiveData<MutableList<TicketEntity>> get() = _ticketsByDate

    suspend fun getAllTicketsByUserId(userId:Int) {
        _allTickets.postValue(repository.getAllTicketsByUserId(userId))
    }

    suspend fun getTicketsByDateAndUser(day: CalendarDay,userId: Int){
        val date = "${day.day}/${day.month}/${day.year}"
        _ticketsByDate.postValue(repository.getTicketsByDateAndUser(date,userId))
    }
}