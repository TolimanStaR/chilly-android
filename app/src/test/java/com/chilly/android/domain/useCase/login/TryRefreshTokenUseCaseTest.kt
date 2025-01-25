package com.chilly.android.domain.useCase.login

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.response.LoginResponse
import com.chilly.android.domain.repository.PreferencesRepository
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.test.runTest
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test

class TryRefreshTokenUseCaseTest {

    private val prefsMock: PreferencesRepository = mockk()
    private val loginMock: LoginApi = mockk()
    private val tokenHolderMock: TokenHolder = mockk()

    private val underTest = TryRefreshTokenUseCase(
        prefsMock,
        loginMock,
        tokenHolderMock
    )

    @AfterEach
    fun tearDown() {
        clearMocks(
            prefsMock, loginMock, tokenHolderMock
        )
    }

    @Test
    fun `when token isn't saved return false`() = runTest {
        coEvery { prefsMock.getSavedRefreshToken() } returns null
        val result = underTest.invoke()
        assertFalse(result)
    }

    @Test
    fun `when token saved and successfully refreshed return true`() = runTest {
        coEvery { prefsMock.getSavedRefreshToken() } returns "r"
        coEvery { loginMock.refresh(any()) } returns Result.success(LoginResponse("a", "r"))
        coEvery { prefsMock.saveRefreshToken(any()) } just Runs
        every { tokenHolderMock.accessToken = any() } just Runs
        val result = underTest.invoke()
        assertTrue(result)
        coVerify { prefsMock.saveRefreshToken(any()) }
        verify { tokenHolderMock.accessToken = any() }
    }

    @Test
    fun `when token saved but couldn't refresh return false`() = runTest {
        coEvery { prefsMock.getSavedRefreshToken() } returns "r"
        coEvery { loginMock.refresh(any()) } returns Result.failure(Throwable())
        val result = underTest.invoke()
        assertFalse(result)
    }
}