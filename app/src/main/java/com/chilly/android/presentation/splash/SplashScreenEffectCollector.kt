package com.chilly.android.presentation.splash

import com.chilly.android.di.screens.SplashScope
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

@SplashScope
class SplashScreenEffectCollector @Inject constructor(
    private val router: Router
): FlowCollector<SplashScreenEffect> {

    override suspend fun emit(value: SplashScreenEffect) {
        when(value) {
            SplashScreenEffect.NavigateLogin -> router.newRootScreen(Destination.LogIn)
            SplashScreenEffect.NavigateMain -> router.newRootScreen(Destination.Main)
            SplashScreenEffect.NavigateOnboarding -> router.newRootScreen(Destination.Onboarding(0))
        }
    }
}