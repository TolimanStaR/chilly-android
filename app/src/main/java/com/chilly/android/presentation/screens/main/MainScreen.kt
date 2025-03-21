package com.chilly.android.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerMainComponent
import com.chilly.android.di.screens.MainComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.PlaceImagesPager
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.main.MainEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MainScreen(
    state: MainState,
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    LaunchedEffect(Unit) {
        onEvent(UiEvent.ScreenIsShown)
    }

    Column (
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(scaffoldPadding)
            .padding(horizontal = 16.dp)
            .fillMaxSize()
    ) {
        PullToRefreshBox(
            isRefreshing = state.isRefreshing,
            onRefresh = {
                onEvent(UiEvent.PulledToRefresh)
            },
            modifier = Modifier.weight(1f)
        ) {
            if (state.feed.isEmpty()) {
                Text(
                    text ="here is going to be shimmer",
                    modifier = Modifier.align(Alignment.Center)
                )
                return@PullToRefreshBox
            }

            val lazyColumnState = rememberLazyListState()
            val layoutInfo by remember {
                derivedStateOf { lazyColumnState.layoutInfo }
            }
            val lastItemVisible = layoutInfo.visibleItemsInfo
                .lastOrNull()?.index == layoutInfo.totalItemsCount - 1

            LaunchedEffect(lastItemVisible) {
                if (lastItemVisible && !state.isLoading) {
                    onEvent(UiEvent.LastFeedElementIsVisible)
                }
            }

            LazyColumn(
                state = lazyColumnState,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item {
                    Text("Places near you")
                }
                items(state.feed) { place ->
                    ElevatedCard(
                        colors = CardDefaults.elevatedCardColors(
                            containerColor = MaterialTheme.colorScheme.surface
                        ),
                        elevation = CardDefaults.cardElevation(4.dp),
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            onEvent(UiEvent.PlaceClicked(place.id))
                        }
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.padding(12.dp)
                        ) {
                            Text(
                                text = place.name,
                                style = MaterialTheme.typography.headlineMedium
                            )
                            PlaceImagesPager(place)
                            Text(
                                text = place.address
                            )
                        }
                    }
                }
                if (state.isLoading) {
                    item {
                        Box(contentAlignment = Alignment.Center, modifier = Modifier.fillMaxWidth()) {
                            CircularProgressIndicator()
                        }
                    }
                }
                item {
                    Spacer(modifier = Modifier.height(4.dp))
                }
            }
        }
        ChillyButton(
            text = stringResource(R.string.main_chily_button),
            onClick = { onEvent(UiEvent.GetRecommendationClicked) },
            size = SizeParameter.Medium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun NavGraphBuilder.installMainScreen(padding: PaddingValues) {
    composable<Destination.Main> {
        ScreenHolder<MainStore, MainComponent>(
            componentFactory = {
                DaggerMainComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            MainScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "MainScreen", showSystemUi = true, showBackground = true)
private fun PreviewMainScreen() {
    ChillyTheme {
        MainScreen(
            state = MainState(),
            PaddingValues(),
            onEvent = {}
        )
    }
}

