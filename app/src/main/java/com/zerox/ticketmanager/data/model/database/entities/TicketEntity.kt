package com.zerox.ticketmanager.data.model.database.entities

import android.location.Address
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "ticket")
data class TicketEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo val id : Int?,
    @ColumnInfo val clientName:String,
    @ColumnInfo val address: String,
    @ColumnInfo val date: String
)
