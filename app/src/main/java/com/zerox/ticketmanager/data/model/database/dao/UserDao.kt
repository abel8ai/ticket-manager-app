package com.zerox.ticketmanager.data.model.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.zerox.ticketmanager.data.model.database.entities.UserEntitiy

@Dao
interface UserDao {

    @Query("Select * from user where id = :id")
    suspend fun getUserById(id:Int):UserEntitiy

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertUser(user : UserEntitiy):Int
}