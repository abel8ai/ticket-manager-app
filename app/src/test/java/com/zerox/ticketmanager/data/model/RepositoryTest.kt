package com.zerox.ticketmanager.data.model

import android.content.Context
import androidx.room.Room
import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.database.TicketManagerDatabase
import com.zerox.ticketmanager.data.model.database.dao.TicketDao
import com.zerox.ticketmanager.data.model.database.dao.UserDao
import com.zerox.ticketmanager.data.model.exceptions.EntityNotFoundException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.assertThrows


public class RepositoryTest {

    @RelaxedMockK
    private lateinit var context: Context
    private lateinit var repository: Repository
    private lateinit var userDao: UserDao
    private lateinit var ticketDao: TicketDao

    @Before
    fun onBefore() {
        MockKAnnotations.init(this)
        //context = InstrumentationRegistry.getInstrumentation().context
        val database = Room.inMemoryDatabaseBuilder(context, TicketManagerDatabase::class.java).build()
        ticketDao = database.getTicketDao()
        userDao = database.getUserDao()
    }

    @Test
    fun returnsEntityNotFoundExceptionIfTheGivenUserDoesntExists() = runBlocking {
        // given
        coEvery { repository.getUserByUsername("pedro") }
            .throws(EntityNotFoundException("There is no user with the provided username"))
        // when
        repository.getUserByUsername("pedro")
        // then
        coVerify(exactly = 1) { userDao.getUserByUsername("pedro") }
        //assertThrows { EntityNotFoundException("There is no user with the provided username") }
    }
}



