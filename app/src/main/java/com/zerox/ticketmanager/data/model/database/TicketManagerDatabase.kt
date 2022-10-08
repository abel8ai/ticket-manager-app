package com.zerox.ticketmanager.data.model.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy

@Database(entities = [UserEntitiy::class], version = 1)
abstract class TicketManagerDatabase:RoomDatabase() {

    abstract fun getUserDao(): UserDao
}