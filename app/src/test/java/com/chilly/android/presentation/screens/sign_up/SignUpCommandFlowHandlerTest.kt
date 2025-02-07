package com.chilly.android.presentation.screens.sign_up

import com.chilly.android.data.remote.HandledException
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.response.ErrorResponse
import com.chilly.android.data.remote.dto.response.LoginResponse
import com.chilly.android.domain.useCase.login.LogInUseCase
import com.chilly.android.presentation.screens.sign_up.SignUpEvent.CommandEvent
import com.chilly.android.utils.testCommandFlow
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.mockk
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class SignUpCommandFlowHandlerTest {

    private val apiMock: LoginApi = mockk()
    private val useCaseMock: LogInUseCase = mockk()

    private val underTest = SignUpCommandFlowHandler(
        apiMock,
        useCaseMock
    )

    @AfterEach
    fun tearDown() {
        clearMocks(
            apiMock, useCaseMock
        )
    }

    @Test
    fun `when signUp succeed Success event is emitted`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(SignUpCommand.SignUp("name", "email", "phone", "password"))
            },
            eventProducer = {
                listOf(CommandEvent.SignUpSuccess("email", "password"))
            },
            configuration = {
                coEvery { apiMock.signUp(any()) } returns Result.success(Unit)
            }
        )
    }


    @Test
    fun `when signUp generally fails, failed event produced`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(SignUpCommand.SignUp("name", "email", "phone", "password"))
            },
            eventProducer = {
                listOf(CommandEvent.SignUpReasonedFail())
            },
            configuration = {
                coEvery { apiMock.signUp(any()) } returns Result.failure(Throwable())
            }
        )
    }

    @Test
    fun `when signUp fails because of conflict, correct exception is produced`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(SignUpCommand.SignUp("name", "email", "phone", "password"))
            },
            eventProducer = {
                listOf(CommandEvent.SignUpReasonedFail(FailReason.DataConflict))
            },
            configuration = {
                coEvery { apiMock.signUp(any()) } returns Result.failure(
                    HandledException(ErrorResponse(statusCode = 409, message = ""))
                )
            }
        )
    }

    @Test
    fun `when login succeed Success event is emitted`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(SignUpCommand.LogIn("email", "password"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginSuccess)
            },
            configuration = {
                coEvery { useCaseMock.invoke(any()) } returns Result.success(LoginResponse("a", "r"))
            }
        )
    }


    @Test
    fun `when login fails general fail emitted`() {
        underTest.testCommandFlow(
            commandsProducer = {
                emit(SignUpCommand.LogIn("email", "password"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginFailure)
            },
            configuration = {
                coEvery { useCaseMock.invoke(any()) } returns Result.failure(Throwable())
            }
        )
    }


}