package com.chilly.android.presentation.screens.result

import androidx.work.WorkRequest
import com.chilly.android.data.remote.dto.PlaceDto

data class RecommendationResultState(
    val isLoading: Boolean = false,
    val recommendations: List<PlaceDto> = emptyList(),
    val errorOccurred: Boolean = false,
    val isPermissionGranted: Boolean = false
)

sealed interface RecommendationResultEvent {
    sealed interface UiEvent : RecommendationResultEvent {
        data object ScreenShown : UiEvent
        data object LoadAgainClicked : UiEvent
        data object CheckRequest : UiEvent
        data class PlaceClicked(val place: PlaceDto) : UiEvent
        data object PermissionGranted : UiEvent
        data class OnPermissionRequestResult(val isGranted: Boolean) : UiEvent
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
    data class NavigatePlace(val place: PlaceDto) : RecommendationResultNews
    data class SubmitNotificationRequest(val request: WorkRequest) : RecommendationResultNews
}

data class MarkerData(
    val title: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0
)