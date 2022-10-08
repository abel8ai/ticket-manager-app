package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.IncorrectPasswordException
import com.zerox.ticketmanager.ui.utils.AESEncyption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {
    private val _userModel = MutableLiveData<UserEntitiy>()
    val userModel: LiveData<UserEntitiy> get() = _userModel

    suspend fun getUserByUsername(username: String) {
        repository.getUserByUsername(username)
    }

    suspend fun doLogin(username: String, password: String) {
        val user = repository.getUserByUsername(username)
        val encryptedPassword = AESEncyption.encrypt(password)
        if (user.password != encryptedPassword)
            throw IncorrectPasswordException("Incorrect password")
        _userModel.postValue(user)
    }
}