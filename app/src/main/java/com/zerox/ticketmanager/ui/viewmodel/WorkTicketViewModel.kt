package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class WorkTicketViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel(){
    // live data to get ticket by id and notify the view
    private val _ticket = MutableLiveData<TicketEntity>()
    val ticket : LiveData<TicketEntity> get() = _ticket

    suspend fun getTicketById(id:Int){
        _ticket.postValue(repository.getTicketById(id))
    }
}