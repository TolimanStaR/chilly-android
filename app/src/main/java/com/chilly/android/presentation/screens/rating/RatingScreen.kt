package com.chilly.android.presentation.screens.rating

import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Up
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.toRoute
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerRatingComponent
import com.chilly.android.di.screens.RatingComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.PlaceCard
import com.chilly.android.presentation.common.components.PlaceRatingDialog
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.slidingComposable
import com.chilly.android.presentation.screens.rating.RatingEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun RatingScreen(
    state: RatingState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    if (state.currentlyRatingPlace != null) {
        PlaceRatingDialog(
            place = state.currentlyRatingPlace,
            onDismiss = {
                onEvent(UiEvent.DialogDismissed)
            },
            onRatingChange = {
                onEvent(UiEvent.RatingChanged(it))
            },
            onCommentChange = {
                onEvent(UiEvent.CommentTextChanged(it))
            },
            onConfirm = {
                onEvent(UiEvent.RatingSent)
            }
        )
    }
    Column (
        verticalArrangement = Arrangement.spacedBy(12.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        state.places.forEach { place ->
            PlaceCard(place, imageSize = 120.dp) {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = place.name,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Text(
                        text = place.address
                    )
                    ChillyButton(
                        text = "Rate ${place.name}",
                        onClick = {
                            onEvent(UiEvent.RateClicked(place))
                        }
                    )
                }
            }
        }
    }
}

fun NavGraphBuilder.installRatingScreen(innerPadding: PaddingValues) {
    slidingComposable<Destination.Rating>(Up) { backStackEntry ->
        val route = backStackEntry.toRoute<Destination.Rating>()
        ScreenHolder<RatingStore, RatingComponent>(
            componentFactory = {
                DaggerRatingComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { storeFactory.create(route.ids) },
            route.ids
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            RatingScreen(state.value, innerPadding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "RatingScreen", showSystemUi = true, showBackground = true)
private fun PreviewRatingScreen() {
    ChillyTheme {
        RatingScreen(
            state = RatingState(emptyList()),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

