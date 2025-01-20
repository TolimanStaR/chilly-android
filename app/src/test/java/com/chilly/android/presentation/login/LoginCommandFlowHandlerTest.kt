package com.chilly.android.presentation.login

import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.response.LoginResponse
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.login.LoginEvent.CommandEvent
import com.chilly.android.utils.testCommandFlow
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


class LoginCommandFlowHandlerTest {

    private val loginApiMock: LoginApi = mockk()
    private val preferencesRepositoryMock: PreferencesRepository = mockk()

    private var underTest: LoginCommandFlowHandler = LoginCommandFlowHandler(
        loginApiMock,
        preferencesRepositoryMock
    )

    @AfterEach
    fun tearDown() {
        clearMocks(
            loginApiMock,
            preferencesRepositoryMock
        )
    }

    @Test
    fun `when login succeed correct event is produced`() {
        val response = LoginResponse("a", "r")
        underTest.testCommandFlow(
            commandsProducer = {
                emit(LoginCommand.LogIn("a", "b"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginSuccess(response.refreshToken, response.accessToken))
            },
            configuration = {
                coEvery { loginApiMock.login(any()) } returns Result.success(response)
                coEvery { preferencesRepositoryMock.saveRefreshToken(any()) } just Runs
            },
            assertions = {
                coVerify { preferencesRepositoryMock.saveRefreshToken(any()) }
            }
        )
    }


    @Test
    fun `when login fails fail event is produced`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(LoginCommand.LogIn("a", "b"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginFail)
            },
            configuration = {
                coEvery { loginApiMock.login(any()) } returns Result.failure(Throwable())
            }
        )
    }
}