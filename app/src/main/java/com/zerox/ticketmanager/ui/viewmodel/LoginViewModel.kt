package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    suspend fun getUserByUsername(username: String) {
        repository.getUserByUsername(username)
    }
}