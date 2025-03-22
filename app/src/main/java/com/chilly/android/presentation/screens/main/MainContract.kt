package com.chilly.android.presentation.screens.main

import android.Manifest
import com.chilly.android.data.remote.dto.PlaceDto

data class MainState(
    val feed: List<PlaceDto> = emptyList(),
    val isRefreshing: Boolean = false,
    val isLoading: Boolean = false,
    val permissionsChecked: Boolean = false,
    val locationAccessGranted: Boolean = false
) {
    companion object {
        val PERMISSIONS = arrayOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    }
}

sealed interface MainEvent {
    sealed interface UiEvent : MainEvent {
        data object GetRecommendationClicked : UiEvent
        data object ScreenIsShown : UiEvent
        data object PulledToRefresh : UiEvent
        data object LastFeedElementIsVisible : UiEvent
        data object PermissionsGranted : UiEvent

        data class PlaceClicked(val placeId: Int) : UiEvent
        data class GotPermissionRequestResult(val permissions: Map<String, Boolean>) : UiEvent
    }

    sealed interface CommandEvent : MainEvent {
        data class CheckQuizResult(val hasBeenCompleted: Boolean) : CommandEvent
        data class FeedUpdated(val newFeed: List<PlaceDto>) : CommandEvent
        data object FeedUpdateFailed : CommandEvent
        data object SameLocationRefresh : CommandEvent
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
    data object SameLocationWasUsed : MainNews
    data class NavigatePlace(val placeId: Int) : MainNews
    data object PermissionsDenied : MainNews
}