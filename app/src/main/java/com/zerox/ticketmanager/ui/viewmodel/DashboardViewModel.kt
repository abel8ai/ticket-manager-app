package com.zerox.ticketmanager.ui.viewmodel

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.ui.utils.DatePickerFragment
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(private val repository: Repository) : ViewModel() {
    private val _ticketsModel = MutableLiveData<MutableList<TicketEntity>>()
    val ticketsModel : LiveData<MutableList<TicketEntity>> get() = _ticketsModel

    private val _ticketModel = MutableLiveData<TicketEntity>()
    val ticketModel : LiveData<TicketEntity> get() = _ticketModel
    suspend fun getAllTickets(){
        _ticketsModel.postValue(repository.getAllTickets())
    }
    suspend fun addTicket(ticket: TicketEntity){
        repository.addTicket(ticket)
        _ticketsModel.postValue(repository.getAllTickets())
    }

    suspend fun getLastTicketCreated(){
        _ticketModel.postValue(repository.getLastTicketCreated())
    }

}