package com.zerox.ticketmanager.data.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy

@Dao
interface TicketDao {

    // queries to get tickets
    @Query("Select * from ticket where id = :id")
    suspend fun getTicketById(id:Int):TicketEntity?

    @Query("Select * from ticket where userId = :userId order by id desc limit 1")
    suspend fun getLastTicketCreatedByUser(userId: Int):TicketEntity?

    @Query("Select * from ticket where date = :date and userId = :userId")
    suspend fun getTicketsByDateAndUser(date:String,userId: Int):MutableList<TicketEntity>

    @Query("Select * from ticket")
    suspend fun getAllTickets():MutableList<TicketEntity>

    @Query("Select * from ticket where userId = :userId")
    suspend fun getAllTicketsByUserId(userId:Int):MutableList<TicketEntity>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertTicket(ticket: TicketEntity):Long
}