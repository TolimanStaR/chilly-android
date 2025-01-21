package com.chilly.android.presentation.onboarding

import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class OnboardingEffectCollector @Inject constructor(
    private val router: Router
) : FlowCollector<OnboardingEffect> {

    override suspend fun emit(value: OnboardingEffect) {
        when(value) {
            is OnboardingEffect.NavigateOnboardingScreen -> router.navigateTo(Destination.Onboarding(value.index))
            OnboardingEffect.OnboardingFinished -> router.newRootScreen(Destination.Main)
        }
    }
}
