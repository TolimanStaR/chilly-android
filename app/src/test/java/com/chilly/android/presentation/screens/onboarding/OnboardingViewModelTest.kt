package com.chilly.android.presentation.screens.onboarding

import com.chilly.android.domain.repository.PreferencesRepository
import io.mockk.Runs
import io.mockk.clearMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.jupiter.api.Assertions.*

import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

@OptIn(ExperimentalCoroutinesApi::class)
@DelicateCoroutinesApi
class OnboardingViewModelTest {

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private val prefsMock: PreferencesRepository = mockk()

    private val underTest = OnboardingViewModel(prefsMock, 10)

    @BeforeEach
    fun setUp() {
        Dispatchers.setMain(mainThreadSurrogate)

        coEvery { prefsMock.setHasSeenOnboarding(any()) } just Runs
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
        mainThreadSurrogate.close()

        clearMocks(prefsMock)
    }

    @Test
    fun `when next step received and there are more steps navigate to next step`() = runTest {
        underTest.dispatch(OnboardingEvent.NextStep(0, 2))
        val effect = underTest.effects.first()
        assertEquals(effect, OnboardingEffect.NavigateOnboardingScreen(1))
    }

    @Test
    fun `when next step received on last step finish onboarding`() = runTest {
        underTest.dispatch(OnboardingEvent.NextStep(1, 2))
        val effect = underTest.effects.first()
        assertEquals(effect, OnboardingEffect.OnboardingFinished)
        coVerify { prefsMock.setHasSeenOnboarding(eq(true))  }
    }

    @Test
    fun `when skip received finish onboarding`() = runTest {
        underTest.dispatch(OnboardingEvent.Finish)
        val effect = underTest.effects.first()
        assertEquals(effect, OnboardingEffect.OnboardingFinished)
        coVerify { prefsMock.setHasSeenOnboarding(eq(true))  }
    }


}