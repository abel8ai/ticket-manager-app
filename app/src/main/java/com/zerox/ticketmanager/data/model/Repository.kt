package com.zerox.ticketmanager.data.model

import com.zerox.ticketmanager.data.model.database.dao.TicketDao
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.NoTicketsException
import com.zerox.ticketmanager.data.model.exceptions.EntityNotFoundException
import com.zerox.ticketmanager.data.model.exceptions.NoTicketsInDateException
import javax.inject.Inject

// repository class to access models
class Repository @Inject constructor(
    private val userDao: UserDao,
    private val ticketDao: TicketDao
) {

    suspend fun getUserById(id: Int): UserEntitiy {
        return userDao.getUserById(id)
            ?: throw EntityNotFoundException("There is no user with the provided id")
    }


    suspend fun getUserByUsername(username: String): UserEntitiy {
        return userDao.getUserByUsername(username)
            ?: throw EntityNotFoundException("There is no user with the provided username")
    }

    suspend fun addUser(user: UserEntitiy): Long {
        return userDao.insertUser(user)
    }

    suspend fun addTicket(ticket: TicketEntity,userId:Int):Long{
        return ticketDao.insertTicket(ticket)
    }

    suspend fun getAllTicketsByUserId(userId: Int):MutableList<TicketEntity>{
        val list = ticketDao.getAllTicketsByUserId(userId)
        if (list.size == 0)
            throw NoTicketsException("There are no tickets for this user")
        else
            return list
    }
    suspend fun getTicketsByDateAndUser(date:String,userId: Int):MutableList<TicketEntity>{
        val list =  ticketDao.getTicketsByDateAndUser(date,userId)
        if (list.size == 0)
            throw NoTicketsInDateException("There are no tickets for this day")
        else
            return list
    }

    suspend fun getLastTicketCreatedByUser(userId: Int):TicketEntity{
        return ticketDao.getLastTicketCreatedByUser(userId)
            ?:throw NoTicketsException("There are no tickets created")
    }

    suspend fun getTicketById(id: Int): TicketEntity {
        return ticketDao.getTicketById(id)
            ?: throw EntityNotFoundException("There is no ticket with the provided id")
    }

    suspend fun updateTicket(ticket: TicketEntity){
        ticketDao.updateTicket(ticket)
    }
    suspend fun deleteTicket(ticket: TicketEntity){
        ticketDao.deleteTicket(ticket)
    }
}