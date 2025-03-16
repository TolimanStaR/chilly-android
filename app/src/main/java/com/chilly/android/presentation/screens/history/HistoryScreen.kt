package com.chilly.android.presentation.screens.history

import android.content.res.Resources
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerHistoryComponent
import com.chilly.android.di.screens.HistoryComponent
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.history.HistoryEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun HistoryScreen(
    state: HistoryUiState,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    if (state.historyItems.isEmpty()) {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Text("History Is Empty")
        }
        return
    }
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        items(state.historyItems) { historyItem ->
            when(historyItem) {
                is HistoryListItem.HistoryDateLabel -> {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        HorizontalDivider(Modifier.weight(1f))
                        Text(
                            text = historyItem.value,
                            modifier = Modifier.padding(8.dp)
                        )
                        HorizontalDivider(Modifier.weight(1f))
                    }
                }
                is HistoryListItem.PlaceItem -> {
                    PlaceListItem(historyItem.value) {
                        onEvent(UiEvent.ItemClicked(historyItem.value.id))
                    }
                }
            }
        }
    }
}

fun NavGraphBuilder.installHistoryScreen(padding: PaddingValues) {
    composable<Destination.History> {
        ScreenHolder<HistoryStore, HistoryComponent>(
            componentFactory = {
                DaggerHistoryComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState(Resources::mapToHistoryUi)
            NewsCollector(component.newsCollector)
            HistoryScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "HistoryScreen", showSystemUi = true, showBackground = true)
private fun PreviewHistoryScreen() {
    ChillyTheme {
        HistoryScreen(
            state = HistoryUiState(),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

