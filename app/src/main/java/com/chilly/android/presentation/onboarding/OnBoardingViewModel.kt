package com.chilly.android.presentation.onboarding

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.navigation.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch


class OnBoardingViewModel @AssistedInject constructor(
    private val preferencesRepository: PreferencesRepository,
    @Assisted private val onNavigate: (Destination, Boolean) -> Unit
) : ViewModel() {

    fun onEvent(event: OnBoardingEvent) {
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
            onNavigate(Destination.OnBoarding(next), false)
        }
    }

    private fun handleFinish() {
        viewModelScope.launch {
            preferencesRepository.setHasSeenOnboarding(true)
        }
        onNavigate(Destination.Main, true)
    }

    @AssistedFactory
    interface Factory {
        fun build(onNavigate: (Destination, Boolean) -> Unit): OnBoardingViewModel
    }
}