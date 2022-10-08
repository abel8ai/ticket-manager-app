package com.zerox.ticketmanager.data.model

import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.UserNotFoundException
import javax.inject.Inject

class Repository @Inject constructor(private val userDao: UserDao) {

    suspend fun getUserById(id: Int): UserEntitiy {
        return userDao.getUserById(id)
            ?: throw UserNotFoundException("There is no user with the provided id")
    }

    suspend fun getUserByUsername(username: String): UserEntitiy {
        return userDao.getUserByUsername(username)
            ?: throw UserNotFoundException("There is no user with the provided username")
    }

    suspend fun addUser(user:UserEntitiy):Long{
        return userDao.insertUser(user)
    }
}