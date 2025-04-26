package com.chilly.android.presentation.screens.onboarding

import com.chilly.android.di.screens.OnboardingScope
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import javax.inject.Inject

@OnboardingScope
class OnboardingEffectCollector @Inject constructor(
    private val router: Router
) : FlowCollector<OnboardingEffect> {

    override suspend fun emit(value: OnboardingEffect) {
        when(value) {
            is OnboardingEffect.NavigateOnboardingScreen -> router.navigateTo(Destination.Onboarding(value.index, value.loggedIn))
            is OnboardingEffect.OnboardingFinished -> {
                Timber.e("finishing onboarding with loggedIn value = ${value.loggedIn}")
                when(value.loggedIn) {
                    true -> router.newRootScreen(Destination.Main)
                    else -> router.newRootScreen(Destination.LogIn)
                }
            }
        }
    }
}
