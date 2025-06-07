package com.chilly.android.presentation.screens.favorites

import com.chilly.android.data.sample.place1
import com.chilly.android.data.sample.place2
import com.chilly.android.data.sample.placeList
import com.chilly.android.utils.testUpdate
import org.junit.jupiter.api.Test

class FavoritesUpdateTest {

    private val underTest = FavoritesUpdate()

    @Test
    fun `when place clicked navigation news is sent`() {
        underTest.testUpdate(
            initialState = FavoritesState(),
            event = FavoritesEvent.UiEvent.PlaceClicked(place1),
            expectedNewsProducer = {
                listOf(FavoritesNews.NavigateToPlace(place1))
            }
        )
    }

    @Test
    fun `when favorites loaded state is updated`() {
        underTest.testUpdate(
            initialState = FavoritesState(favorites = emptyList()),
            event = FavoritesEvent.CommandEvent.FavoritesLoaded(placeList),
            expectedStateProducer = {
                state.copy(favorites = placeList)
            }
        )
    }

    @Test
    fun `when favorites loaded with empty list state reflects empty list`() {
        underTest.testUpdate(
            initialState = FavoritesState(favorites = placeList),
            event = FavoritesEvent.CommandEvent.FavoritesLoaded(emptyList()),
            expectedStateProducer = {
                state.copy(favorites = emptyList())
            }
        )
    }

    @Test
    fun `when favorites loaded state is replaced not appended`() {
        val initialPlaces = listOf(place1)
        val newPlaces = listOf(place2)

        underTest.testUpdate(
            initialState = FavoritesState(favorites = initialPlaces),
            event = FavoritesEvent.CommandEvent.FavoritesLoaded(newPlaces),
            expectedStateProducer = {
                state.copy(favorites = newPlaces)
            }
        )
    }
}