package com.chilly.android.presentation.screens.splash

sealed interface SplashScreenEffect {
    data object NavigateOnboarding : SplashScreenEffect
    data object NavigateLogin : SplashScreenEffect
    data object NavigateMain : SplashScreenEffect
}