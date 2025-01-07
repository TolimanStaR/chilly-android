package com.chilly.android.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.login.TryRefreshTokenUseCase
import com.chilly.android.presentation.navigation.Destination
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SplashScreenViewModel @AssistedInject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val tokenUseCase: TryRefreshTokenUseCase,
    @Assisted private val onNavigate: (Destination) -> Unit
) : ViewModel() {


    fun tryLogin() {
        viewModelScope.launch {
            val loggedIn = tokenUseCase.invoke()
            if (loggedIn) {
                tryOnboarding()
            } else {
                onNavigate(Destination.LogIn)
            }
        }
    }

    private suspend fun tryOnboarding() {
        if (preferencesRepository.hasSeenOnboarding()) {
            onNavigate(Destination.Main)
        } else {
            onNavigate(Destination.OnBoarding(index = 0))
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(onNavigate: (Destination) -> Unit): SplashScreenViewModel
    }
}