package com.zerox.ticketmanager.data.model

import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import javax.inject.Inject

class Repository @Inject constructor(private val userDao: UserDao) {

    suspend fun getUserById(id:Int):UserEntitiy{
        return userDao.getUserById(id)
    }

    suspend fun getUserByUsername(username:String):UserEntitiy{
        return userDao.getUserByUsername(username)
    }

    suspend fun addUser(user:UserEntitiy):Int{
        return userDao.insertUser(user)
    }
}