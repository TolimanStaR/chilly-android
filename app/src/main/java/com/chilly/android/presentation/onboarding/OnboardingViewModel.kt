package com.chilly.android.presentation.onboarding

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import kotlinx.coroutines.launch
import javax.inject.Inject


class OnboardingViewModel(
    private val preferencesRepository: PreferencesRepository,
    @VisibleForTesting effectReply: Int
) : ViewModelWithEffects<OnboardingEffect, OnboardingEvent>(effectReply) {

    @Inject
    constructor(
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

}