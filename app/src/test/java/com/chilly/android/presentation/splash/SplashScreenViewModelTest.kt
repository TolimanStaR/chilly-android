package com.chilly.android.presentation.splash

import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.login.TryRefreshTokenUseCase
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
@DelicateCoroutinesApi
class SplashScreenViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()
    }

    @Test
    fun `when token cannot be refreshed navigate to login screen`() = runTest {
        val prefsMock: PreferencesRepository = mockk()
        val tokenUCMock: TryRefreshTokenUseCase = mockk()

        coEvery { tokenUCMock.invoke() } returns false

        val underTest = SplashScreenViewModel(prefsMock, tokenUCMock, 10)

        val effect = underTest.effects.first()
        assertEquals(effect, SplashScreenEffect.NavigateLogin)
    }

    @Test
    fun `when token refreshed and onboarding hasn't been seen navigate to onboarding`() = runTest {
        val prefsMock: PreferencesRepository = mockk()
        val tokenUCMock: TryRefreshTokenUseCase = mockk()

        coEvery { tokenUCMock.invoke() } returns true
        coEvery { prefsMock.hasSeenOnboarding() } returns false

        val underTest = SplashScreenViewModel(prefsMock, tokenUCMock, 10)

        val effect = underTest.effects.first()
        assertEquals(effect, SplashScreenEffect.NavigateOnboarding)
    }

    @Test
    fun `when token refreshed and seen onboarding navigate to main`() = runTest {
        val prefsMock: PreferencesRepository = mockk()
        val tokenUCMock: TryRefreshTokenUseCase = mockk()

        coEvery { tokenUCMock.invoke() } returns true
        coEvery { prefsMock.hasSeenOnboarding() } returns true

        val underTest = SplashScreenViewModel(prefsMock, tokenUCMock, 10)

        val effect = underTest.effects.first()
        assertEquals(effect, SplashScreenEffect.NavigateMain)
    }
}