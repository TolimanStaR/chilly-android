package com.chilly.android.presentation.onboarding

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class OnboardingViewModel(
    private val preferencesRepository: PreferencesRepository,
    @VisibleForTesting effectReply: Int
) : ViewModelWithEffects<OnboardingEffect, OnboardingEvent>(effectReply) {

    @AssistedInject constructor(
        preferencesRepository: PreferencesRepository
    ) : this(preferencesRepository, 0)

    override fun dispatch(event: OnboardingEvent) {
        when(event) {
            is OnboardingEvent.NextStep -> handleNextStep(event)
            OnboardingEvent.Finish -> handleFinish()
        }
    }

    private fun handleNextStep(event: OnboardingEvent.NextStep) {
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
        fun build(): OnboardingViewModel
    }
}