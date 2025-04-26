package com.chilly.android.presentation.screens.onboarding

sealed interface OnboardingEffect {
    data class NavigateOnboardingScreen(val index: Int, val loggedIn: Boolean) : OnboardingEffect
    data class OnboardingFinished(val loggedIn: Boolean) : OnboardingEffect
}

sealed interface OnboardingEvent {
    class NextStep(val current: Int, val loggedIn: Boolean, val count: Int) : OnboardingEvent
    data class Finish(val loggedIn: Boolean = false) : OnboardingEvent
}