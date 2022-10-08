package com.zerox.ticketmanager.data.model

import com.zerox.ticketmanager.data.model.database.dao.TicketDao
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy
import com.zerox.ticketmanager.data.model.exceptions.NoTicketsInDatabseException
import com.zerox.ticketmanager.data.model.exceptions.EntityNotFoundException
import javax.inject.Inject

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

    suspend fun addTicket(ticket: TicketEntity):Long{
        return ticketDao.insertTicket(ticket)
    }

    suspend fun getAllTickets():MutableList<TicketEntity>{
        return ticketDao.getAllTickets()
    }
    suspend fun getLastTicketCreated():TicketEntity{
        return ticketDao.getLastTicketCreated()
            ?:throw NoTicketsInDatabseException("There are no tickets ins the database")
    }

    suspend fun getTicketById(id: Int): TicketEntity {
        return ticketDao.getTicketById(id)
            ?: throw EntityNotFoundException("There is no ticket with the provided id")
    }
}