package com.chilly.android.presentation.screens.place

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.toRoute
import coil3.compose.SubcomposeAsyncImage
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.PlaceInfoComponent
import com.chilly.android.di.screens.DaggerPlaceInfoComponent
import com.chilly.android.presentation.common.components.ErrorReloadPlaceHolder
import com.chilly.android.presentation.common.components.LoadingPlaceholder
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.place.PlaceInfoEvent.UiEvent

@Composable
private fun PlaceInfoScreen(
    state: PlaceInfoState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    when {
        state.place == null && !state.errorOccurred -> {
            LoadingPlaceholder(null) {
                onEvent(UiEvent.ShownLoading)
            }
            return
        }
        state.place == null && state.errorOccurred -> {
            ErrorReloadPlaceHolder(null) {
                onEvent(UiEvent.ReloadPlace)
            }
            return
        }
    }
    if (state.place == null) return

    Column(
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
    ) {
        SubcomposeAsyncImage(
            model = state.place.imageUrls.firstOrNull(),
            contentDescription = null,
            contentScale = ContentScale.Fit,
            modifier = Modifier.fillMaxSize(),
            loading = {
                Box {
                    CircularProgressIndicator()
                }
            }
        )
        Text(
            text = state.place.name,
            style = MaterialTheme.typography.headlineMedium
        )
        Text(text = state.place.address)
        IconButton(
            onClick = {
                onEvent(UiEvent.ToggleFavoriteClicked)
            }
        ) {
            val icon = if (state.isInFavorites) {
                Icons.Filled.Favorite
            } else {
                Icons.Outlined.FavoriteBorder
            }
            val color = if (state.isInFavorites) Color.Yellow else MaterialTheme.colorScheme.onSurface
            Icon(imageVector = icon, contentDescription = null, tint = color)
        }
    }
}

fun NavGraphBuilder.installPlaceInfoScreen(padding: PaddingValues) {
    composable<Destination.PlaceInfo> { backStackEntry ->
        val route = backStackEntry.toRoute<Destination.PlaceInfo>()

        ScreenHolder<PlaceInfoStore, PlaceInfoComponent>(
            componentFactory = {
                DaggerPlaceInfoComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { storeFactory.create(route.id) },
            route.id
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            PlaceInfoScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "PlaceInfoScreen", showSystemUi = true, showBackground = true)
private fun PreviewPlaceInfoScreen() {
    ChillyTheme {
        PlaceInfoScreen(
            state = PlaceInfoState(-1),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

