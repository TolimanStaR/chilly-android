package com.chilly.android.presentation.screens.result

import com.chilly.android.data.remote.dto.PlaceDto

data class RecommendationResultState(
    val isLoading: Boolean = false,
    val recommendations: List<PlaceDto> = emptyList(),
    val errorOccurred: Boolean = false
)

sealed interface RecommendationResultEvent {
    sealed interface UiEvent : RecommendationResultEvent {
        data object ScreenShown : UiEvent
        data object LoadAgainClicked : UiEvent
        data object CheckRequest : UiEvent
    }

    sealed interface CommandEvent : RecommendationResultEvent {
        data class LoadingSuccess(val recommendations: List<PlaceDto>) : CommandEvent
        data object LoadingFail : CommandEvent
        data object ClearData : CommandEvent
    }
}

sealed interface RecommendationResultCommand {
    data object CheckRequested : RecommendationResultCommand
    data object LoadRecommendations : RecommendationResultCommand
}

sealed interface RecommendationResultNews {
    data object GeneralFail : RecommendationResultNews
    data object Navigate : RecommendationResultNews
}