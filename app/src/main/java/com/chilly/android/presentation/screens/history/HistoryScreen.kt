package com.chilly.android.presentation.screens.history

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import com.chilly.android.di.screens.HistoryComponent
import com.chilly.android.di.screens.DaggerHistoryComponent
import com.chilly.android.presentation.common.components.PlaceListItem
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.history.HistoryEvent.UiEvent
import java.text.SimpleDateFormat
import java.util.Locale

@Composable
private fun HistoryScreen(
    state: HistoryState,
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

    val formatter = remember { SimpleDateFormat("HH:mm, dd.MM.yyyy", Locale.getDefault()) }

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
    ) {
        items(state.historyItems) { historyItem ->
            Text(text = formatter.format(historyItem.timestamp))
            PlaceListItem(historyItem.place) {
                onEvent(UiEvent.ItemClicked(historyItem.place.id))
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
            val state = collectState()
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
            state = HistoryState(),
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

