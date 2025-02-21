package com.chilly.android.presentation.screens.main

sealed interface MainEvent {
    sealed interface UiEvent : MainEvent {
        data object GetRecommendationClicked : UiEvent
    }

    sealed interface CommandEvent : MainEvent {
        data class CheckQuizResult(val hasBeenCompleted: Boolean) : CommandEvent
    }
}

sealed interface MainCommand {
    data object CheckMainQuiz : MainCommand
}

sealed interface MainNews {
    data object NavigateLogin : MainNews
    data object NavigateOnboarding : MainNews
    data object NavigateMainQuiz : MainNews
    data object NavigateShortQuiz : MainNews
}