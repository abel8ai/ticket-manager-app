package com.zerox.ticketmanager.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zerox.ticketmanager.data.model.database.dao.TicketDao
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.TicketEntity
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy

// database configuration with Data Access Object to user and ticket tables
@Database(entities = [UserEntitiy::class,TicketEntity::class], version = 1)
abstract class TicketManagerDatabase:RoomDatabase() {

    abstract fun getUserDao(): UserDao

    abstract fun getTicketDao(): TicketDao
}