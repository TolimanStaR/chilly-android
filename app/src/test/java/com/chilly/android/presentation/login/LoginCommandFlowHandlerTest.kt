package com.chilly.android.presentation.login

import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.data.remote.dto.request.RefreshRequest
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

    private val preferencesRepositoryMock: PreferencesRepository = mockk()

    private lateinit var underTest: LoginCommandFlowHandler

    private fun stubUnderTest(loginApi: LoginApi) {
        underTest = LoginCommandFlowHandler(
            loginApi,
            preferencesRepositoryMock
        )
    }

    @AfterEach
    fun tearDown() {
        clearMocks(
            preferencesRepositoryMock
        )
    }

    @Test
    fun `when login succeed correct event is produced`() {
        val response = LoginResponse("a", "r")
        stubUnderTest(object : FakeApi() {
            override suspend fun login(request: LoginRequest): Result<LoginResponse> = Result.success(response)
        })

        underTest.testCommandFlow(
            commandsProducer = {
                emit(LoginCommand.LogIn("a", "b"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginSuccess(response.refreshToken, response.accessToken))
            },
            configuration = {
                coEvery { preferencesRepositoryMock.saveRefreshToken(any()) } just Runs
            },
            assertions = {
                coVerify { preferencesRepositoryMock.saveRefreshToken(any()) }
            }
        )
    }


    @Test
    fun `when login fails fail event is produced`() {
        stubUnderTest(FakeApi())
        underTest.testCommandFlow(
            commandsProducer = {
                emit(LoginCommand.LogIn("a", "b"))
            },
            eventProducer = {
                listOf(CommandEvent.LoginFail)
            }
        )
    }

    private open class FakeApi : LoginApi {
        override suspend fun login(request: LoginRequest): Result<LoginResponse> = Result.failure(Throwable())
        override suspend fun refresh(request: RefreshRequest): Result<LoginResponse> = Result.failure(Throwable())
    }
}