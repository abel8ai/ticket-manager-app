package com.zerox.ticketmanager.data.model.database

import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import javax.inject.Inject

class Repository @Inject constructor(private val userDao: UserDao) {

    suspend fun getUserById(id:Int):UserEntitiy{
        return userDao.getUserById(id)
    }

    suspend fun addUser(user:UserEntitiy):Int{
        return userDao.insertUser(user)
    }
}