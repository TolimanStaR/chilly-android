package com.chilly.android.presentation.screens.splash

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.login.TryRefreshTokenUseCase
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import kotlinx.coroutines.launch
import javax.inject.Inject

class SplashScreenViewModel(
    private val preferencesRepository: PreferencesRepository,
    private val tokenUseCase: TryRefreshTokenUseCase,
    private val collector: SplashScreenEffectCollector,
    @VisibleForTesting effectReply: Int
): ViewModelWithEffects<SplashScreenEffect, Nothing>(effectReply) {

    @Inject
    constructor(
        preferencesRepository: PreferencesRepository,
        tokenUseCase: TryRefreshTokenUseCase,
        collector: SplashScreenEffectCollector
    ) : this(preferencesRepository, tokenUseCase, collector, 0)

    init {
        viewModelScope.launch {
            effects.collect(collector)
        }

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

    override fun dispatch(event: Nothing) = Unit
}