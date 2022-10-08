package com.zerox.ticketmanager.data.model

import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.UserNotFoundException
import javax.inject.Inject

class Repository @Inject constructor(private val userDao: UserDao) {

    suspend fun getUserById(id:Int):UserEntitiy{
        val user = userDao.getUserById(id)
        if (user == null)
            throw UserNotFoundException("There is no user with the provided id")
        return user
    }

    suspend fun getUserByUsername(username:String):UserEntitiy{
        val user = userDao.getUserByUsername(username)
        if (user == null)
            throw UserNotFoundException("There is no user with the provided username")
        return user
    }

    suspend fun addUser(user:UserEntitiy):Long{
        return userDao.insertUser(user)
    }
}