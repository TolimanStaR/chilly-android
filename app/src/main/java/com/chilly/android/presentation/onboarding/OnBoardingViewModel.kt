package com.chilly.android.presentation.onboarding

import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class OnBoardingViewModel @AssistedInject constructor(
    private val preferencesRepository: PreferencesRepository
) : ViewModelWithEffects<OnboardingEffect, OnBoardingEvent>() {

    override fun dispatch(event: OnBoardingEvent) {
        when(event) {
            is OnBoardingEvent.NextStep -> handleNextStep(event)
            OnBoardingEvent.Finish -> handleFinish()
        }
    }

    private fun handleNextStep(event: OnBoardingEvent.NextStep) {
        val next = event.current + 1
        if (next >= event.count) {
            handleFinish()
        } else {
            emit(OnboardingEffect.NavigateOnboardingScreen(next))
        }
    }

    private fun handleFinish() {
        viewModelScope.launch {
            preferencesRepository.setHasSeenOnboarding(true)
        }
        emit(OnboardingEffect.OnboardingFinished)
    }

    @AssistedFactory
    interface Factory {
        fun build(): OnBoardingViewModel
    }
}