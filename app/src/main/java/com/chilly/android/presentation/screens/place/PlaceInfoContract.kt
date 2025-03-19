package com.chilly.android.presentation.screens.place

import com.chilly.android.data.remote.dto.PlaceDto

data class PlaceInfoState(
    val placeId: Int,
    val place: PlaceDto? = null,
    val isInFavorites: Boolean = false,
    val errorOccurred: Boolean = false,
    val expandedSections: Set<Section> = emptySet()
)

sealed interface PlaceInfoEvent {
    sealed interface UiEvent : PlaceInfoEvent {
        data object ShownLoading : UiEvent
        data object ReloadPlace : UiEvent
        data object ToggleFavoriteClicked : UiEvent
        data class ToggleExpansion(val section: Section) : UiEvent
        data object BackClicked : UiEvent
    }

    sealed interface CommandEvent : PlaceInfoEvent {
        data class LoadSuccess(val place: PlaceDto) : CommandEvent
        data object LoadFail : CommandEvent
        data class FavoritesCheckResult(val inFavorites: Boolean) : CommandEvent
    }
}

sealed interface PlaceInfoCommand {
    data class LoadPlace(val id: Int) : PlaceInfoCommand
    data class CheckFavorite(val placeId: Int): PlaceInfoCommand
    data class ToggleFavorites(val placeId: Int, val currentlyInFavorites: Boolean) : PlaceInfoCommand
}

sealed interface PlaceInfoNews {
    data object NavigateUp : PlaceInfoNews
    data object GeneralFail : PlaceInfoNews
}

enum class Section {
    CONTACTS,
    OPEN_HOURS
}