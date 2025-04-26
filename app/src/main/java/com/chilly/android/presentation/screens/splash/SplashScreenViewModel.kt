package com.chilly.android.presentation.screens.splash

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.viewModelScope
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.login.TryRefreshTokenUseCase
import com.chilly.android.presentation.common.structure.ViewModelWithEffects
import kotlinx.coroutines.launch
import timber.log.Timber
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

    override fun dispatch(event: SplashScreenEvent) = when(event) {
        is SplashScreenEvent.GotNotificationEvent -> loadScreen(SplashScreenEffect.NavigateRating(event.recIds))
        SplashScreenEvent.GotRegularIntent -> loadScreen(SplashScreenEffect.NavigateMain)
    }

    private fun loadScreen(destinationEffect: SplashScreenEffect) {
        viewModelScope.launch {
            val loggedIn = tokenUseCase.invoke()
            Timber.e("checked logged state")
            tryOnboarding(destinationEffect, loggedIn)
        }
    }

    private suspend fun tryOnboarding(destinationEffect: SplashScreenEffect, isLogged: Boolean) {
        Timber.e("before trying onboarding")
        val alreadySeenOnboarding = preferencesRepository.hasSeenOnboarding()
        Timber.e("has seen onboarding value: $alreadySeenOnboarding")
        when {
            alreadySeenOnboarding && isLogged -> emit(destinationEffect)
            !alreadySeenOnboarding -> emit(SplashScreenEffect.NavigateOnboarding(isLogged))
            else -> emit(SplashScreenEffect.NavigateLogin)
        }
    }
}