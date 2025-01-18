package com.chilly.android.presentation.splash

import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.login.TryRefreshTokenUseCase
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.launch

class SplashScreenViewModel @AssistedInject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val tokenUseCase: TryRefreshTokenUseCase
): ViewModelWithEffects<SplashScreenEffect, Nothing>() {

    init {
        viewModelScope.launch {
            val loggedIn = tokenUseCase.invoke()
            if (loggedIn) {
                tryOnboarding()
            } else {
                emit(SplashScreenEffect.NavigateLogin)
            }
        }
    }

    private suspend fun tryOnboarding() {
        if (preferencesRepository.hasSeenOnboarding()) {
            emit(SplashScreenEffect.NavigateMain)
        } else {
            emit(SplashScreenEffect.NavigateOnboarding)
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(): SplashScreenViewModel
    }

    override fun dispatch(event: Nothing) = Unit
}