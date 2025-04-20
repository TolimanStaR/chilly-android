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
): ViewModelWithEffects<SplashScreenEffect, SplashScreenEvent>(effectReply) {

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
    }

    private suspend fun tryOnboarding(mainDestination: SplashScreenEffect) {
        if (preferencesRepository.hasSeenOnboarding()) {
            emit(mainDestination)
        } else {
            emit(SplashScreenEffect.NavigateOnboarding)
        }
    }

    private fun loadScreen(mainDestination: SplashScreenEffect) {
        viewModelScope.launch {
            val loggedIn = tokenUseCase.invoke()
            if (loggedIn) {
                tryOnboarding(mainDestination)
            } else {
                emit(SplashScreenEffect.NavigateLogin)
            }
        }
    }

    override fun dispatch(event: SplashScreenEvent) = when(event) {
        is SplashScreenEvent.GotNotificationEvent -> loadScreen(SplashScreenEffect.NavigateRating(event.recIds))
        SplashScreenEvent.GotRegularIntent -> loadScreen(SplashScreenEffect.NavigateMain)
    }
}