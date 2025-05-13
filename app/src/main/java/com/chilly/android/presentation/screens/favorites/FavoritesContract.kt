package com.chilly.android.presentation.screens.favorites

import com.chilly.android.data.remote.dto.PlaceDto

data class FavoritesState(
    val favorites: List<PlaceDto> = emptyList()
)

sealed interface FavoritesEvent {
    sealed interface UiEvent : FavoritesEvent {
        data class PlaceClicked(val place: PlaceDto) : UiEvent
    }

    sealed interface CommandEvent : FavoritesEvent {
        data class FavoritesLoaded(val favorites: List<PlaceDto>) : CommandEvent
    }
}

sealed interface FavoritesCommand {
    data object LoadFavorites : FavoritesCommand
}

sealed interface FavoritesNews {
    data class NavigateToPlace(val place: PlaceDto) : FavoritesNews
}