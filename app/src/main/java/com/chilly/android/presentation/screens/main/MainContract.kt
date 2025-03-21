package com.chilly.android.presentation.screens.main

import com.chilly.android.data.remote.dto.PlaceDto

data class MainState(
    val feed: List<PlaceDto> = emptyList(),
    val isLoading: Boolean = false
)

sealed interface MainEvent {
    sealed interface UiEvent : MainEvent {
        data object GetRecommendationClicked : UiEvent
        data object ScreenIsShown : UiEvent
        data object PulledToRefresh : UiEvent
        data object LastFeedElementIsVisible : UiEvent
    }

    sealed interface CommandEvent : MainEvent {
        data class CheckQuizResult(val hasBeenCompleted: Boolean) : CommandEvent
        data class FeedUpdated(val newFeed: List<PlaceDto>) : CommandEvent
        data object FeedUpdateFailed : CommandEvent
    }
}

sealed interface MainCommand {
    data object CheckMainQuiz : MainCommand
    data object RefreshFeed : MainCommand
    data object LoadFeed : MainCommand
    data object LoadNewFeedPage : MainCommand
}

sealed interface MainNews {
    data object NavigateLogin : MainNews
    data object NavigateOnboarding : MainNews
    data object NavigateMainQuiz : MainNews
    data object NavigateShortQuiz : MainNews
    data object GeneralFail : MainNews
}