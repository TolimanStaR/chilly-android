package com.chilly.android.presentation.onboarding

sealed interface OnBoardingEvent {
    class NextStep(val current: Int, val count: Int) : OnBoardingEvent
    data object Finish : OnBoardingEvent
}