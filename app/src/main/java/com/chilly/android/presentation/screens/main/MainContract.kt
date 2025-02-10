package com.chilly.android.presentation.screens.main

class MainState(
    val text: String = ""
)

sealed interface MainEvent {
    sealed interface UiEvent : MainEvent {
        data object SignOutClicked : UiEvent
        data object ToOnboardingClicked : UiEvent
    }

    sealed interface CommandEvent : MainEvent {
        data object Success : CommandEvent
        data object Fail : CommandEvent
    }
}

sealed interface MainCommand {
    data object Load : MainCommand
}

sealed interface MainNews {
    data object NavigateLogin : MainNews
    data object NavigateOnboarding : MainNews
}