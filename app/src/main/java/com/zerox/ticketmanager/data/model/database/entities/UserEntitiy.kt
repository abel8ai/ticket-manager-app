package com.zerox.ticketmanager.data.model.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user")
data class UserEntitiy(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id:Int?,
    @ColumnInfo val username : String,
    @ColumnInfo val password : String
)
