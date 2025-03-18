package com.chilly.android.presentation.screens.favorites

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.FavoritesComponent
import com.chilly.android.di.screens.DaggerFavoritesComponent
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.favorites.FavoritesEvent.UiEvent

@Composable
private fun FavoritesScreen(
    state: FavoritesState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    if (state.favorites.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("Favorites Are Empty")
        }
        return
    }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        items(state.favorites) { place ->
            PlaceListItem(place) {
                onEvent(UiEvent.PlaceClicked(place.id))
            }
        }
        item {
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

fun NavGraphBuilder.installFavoritesScreen(padding: PaddingValues) {
    composable<Destination.Favorites> {
        ScreenHolder<FavoritesStore, FavoritesComponent>(
            componentFactory = {
                DaggerFavoritesComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            FavoritesScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "FavoritesScreen", showSystemUi = true, showBackground = true)
private fun PreviewFavoritesScreen() {
    ChillyTheme {
        FavoritesScreen(
            state = FavoritesState(),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

