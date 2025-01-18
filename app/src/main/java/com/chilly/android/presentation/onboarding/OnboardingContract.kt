package com.chilly.android.presentation.onboarding

sealed interface OnboardingEffect {
    class NavigateOnboardingScreen(val index: Int) : OnboardingEffect
    data object OnboardingFinished : OnboardingEffect
}

sealed interface OnBoardingEvent {
    class NextStep(val current: Int, val count: Int) : OnBoardingEvent
    data object Finish : OnBoardingEvent
}