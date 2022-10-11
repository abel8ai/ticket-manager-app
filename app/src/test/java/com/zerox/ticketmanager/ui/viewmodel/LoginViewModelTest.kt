package com.zerox.ticketmanager.ui.viewmodel

import com.zerox.ticketmanager.data.model.Repository
import com.zerox.ticketmanager.data.model.exceptions.EntityNotFoundException
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.RelaxedMockK
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test


class LoginViewModelTest {
    @RelaxedMockK
    private lateinit var repository: Repository
    lateinit var loginViewModel: LoginViewModel

    @Before
    fun onBefore(){
        MockKAnnotations.init(this)
        loginViewModel = LoginViewModel(repository)
    }

    @Test
    fun returnsEntityNotFoundExceptionIfTheGivenUserDoesntExists() = runBlocking {
        // given
        coEvery { repository.getUserByUsername("pedro") }
            .throws(EntityNotFoundException("There is no user with the provided username"))
        // when
        loginViewModel.getUserByUsername("pedro")
        // then
        coVerify (exactly = 1) { throw EntityNotFoundException("There is no user with the provided username") }
    }
}
