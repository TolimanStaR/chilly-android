package com.chilly.android.presentation.screens.onboarding

sealed interface OnboardingEffect {
    data class NavigateOnboardingScreen(val index: Int) : OnboardingEffect
    data object OnboardingFinished : OnboardingEffect
}

sealed interface OnboardingEvent {
    class NextStep(val current: Int, val count: Int) : OnboardingEvent
    data object Finish : OnboardingEvent
}