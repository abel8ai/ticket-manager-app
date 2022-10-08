package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _ticketModel = MutableLiveData<MutableList<TicketEntity>>()
    val ticketModel : LiveData<MutableList<TicketEntity>> get() = _ticketModel

    suspend fun getAllTickets(){
        _ticketModel.postValue(repository.getAllTickets())
    }
    suspend fun addTicket(ticket: TicketEntity){
        repository.addTicket(ticket)
        _ticketModel.postValue(repository.getAllTickets())
    }
}