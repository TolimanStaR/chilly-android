package com.chilly.android.presentation.screens.splash

sealed interface SplashScreenEffect {
    data class NavigateOnboarding(val isLogged: Boolean) : SplashScreenEffect
    data object NavigateLogin : SplashScreenEffect
    data object NavigateMain : SplashScreenEffect
    data class NavigateRating(val ids: List<Int>) : SplashScreenEffect
}

sealed interface SplashScreenEvent {
    data object GotRegularIntent : SplashScreenEvent
    data class GotNotificationEvent(val recIds: List<Int>) : SplashScreenEvent
}