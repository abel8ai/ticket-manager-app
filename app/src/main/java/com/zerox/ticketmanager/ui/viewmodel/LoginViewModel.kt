package com.zerox.ticketmanager.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.EncryptionErrorException
import com.zerox.ticketmanager.data.model.exceptions.IncorrectPasswordException
import com.zerox.ticketmanager.ui.utils.AESEncyption
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(
    private val repository: Repository
) : ViewModel() {

    // livedata to notify the activity when the data is ready
    private val _user = MutableLiveData<UserEntitiy>()
    val user: LiveData<UserEntitiy> get() = _user

    suspend fun getUserByUsername(username: String) {
        repository.getUserByUsername(username)
    }

    suspend fun doLogin(username: String, password: String) {
        val user = repository.getUserByUsername(username)
        // encrypt password to compare with encrypted password saved in database
        // added exception management in the rare case the encryption process fails
        val encryptedPassword = AESEncyption.encrypt(password)
            ?: throw EncryptionErrorException("There was an error while encrypting")

        if (user.password != encryptedPassword)
            throw IncorrectPasswordException("Incorrect password")
        _user.postValue(user)
    }

    suspend fun addDummyUser(){
        // encrypt password to save into database
        // added exception management in the rare case the encryption process fails
        val pass = AESEncyption.encrypt("123")
            ?: throw EncryptionErrorException("There was an error while encrypting")

        repository.addUser(UserEntitiy(null,"peter",pass))
    }
}