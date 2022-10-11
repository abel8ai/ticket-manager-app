package com.zerox.ticketmanager.data.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy

@Dao
interface TicketDao {

    @Query("Select * from ticket where id = :id")
    suspend fun getTicketById(id:Int):TicketEntity?

    @Query("Select * from ticket order by id desc limit 1")
    suspend fun getLastTicketCreated():TicketEntity?

    @Query("Select * from ticket where date = :date")
    suspend fun getTicketsByDate(date:String):MutableList<TicketEntity>

    @Query("Select * from ticket")
    suspend fun getAllTickets():MutableList<TicketEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTicket(ticket: TicketEntity):Long
}