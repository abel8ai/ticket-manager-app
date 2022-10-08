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
    private val _ticketModel = MutableLiveData<TicketEntity>()
    val ticketModel : LiveData<TicketEntity> get() = _ticketModel

    suspend fun getTicketById(id:Int){
        _ticketModel.postValue(repository.getTicketById(id))
    }
}