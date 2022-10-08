package com.zerox.ticketmanager.di

import android.app.Application
import androidx.room.Room
import com.zerox.ticketmanager.data.model.database.TicketManagerDatabase
import com.zerox.ticketmanager.data.model.database.dao.TicketDao
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(appContext:Application):TicketManagerDatabase{
        return Room.databaseBuilder(appContext, TicketManagerDatabase::class.java,"database").build()
    }

    @Provides
    @Singleton
    fun provideUserDao(database: TicketManagerDatabase):UserDao{
        return database.getUserDao()
    }
    @Provides
    @Singleton
    fun provideTicketDao(database: TicketManagerDatabase):TicketDao{
        return database.getTicketDao()
    }
}