package com.chilly.android.presentation.screens.favorites

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class FavoritesUpdate @Inject constructor(

) : DslUpdate<FavoritesState, FavoritesEvent, FavoritesCommand, FavoritesNews>() {

    override fun NextBuilder.update(event: FavoritesEvent) = when (event) {
        is FavoritesEvent.UiEvent -> updateOnUi(event)
        is FavoritesEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: FavoritesEvent.UiEvent) {
        when (event) {
            is FavoritesEvent.UiEvent.PlaceClicked -> {
                news(FavoritesNews.NavigateToPlace(event.place))
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: FavoritesEvent.CommandEvent) {
        when (event) {
            is FavoritesEvent.CommandEvent.FavoritesLoaded -> {
                state { copy(favorites = event.favorites) }
            }
        }
    }
}
