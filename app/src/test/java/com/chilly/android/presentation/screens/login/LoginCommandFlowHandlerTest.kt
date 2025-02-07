package com.chilly.android.presentation.screens.login

import com.chilly.android.data.remote.dto.response.LoginResponse
import com.chilly.android.domain.useCase.login.LogInUseCase
import com.chilly.android.presentation.screens.login.LoginEvent.CommandEvent
import com.chilly.android.utils.testCommandFlow
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test


class LoginCommandFlowHandlerTest {

    private val loginUseCaseMock: LogInUseCase = mockk()

    private var underTest: LoginCommandFlowHandler = LoginCommandFlowHandler(
        loginUseCaseMock
    )

    @AfterEach
    fun tearDown() {
        clearMocks(
            loginUseCaseMock
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
                coEvery { loginUseCaseMock.invoke(any()) } returns Result.success(response)
            },
            assertions = {
                coVerify { loginUseCaseMock.invoke(any()) }
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
                coEvery { loginUseCaseMock.invoke(any()) } returns Result.failure(Throwable())
            }
        )
    }
}