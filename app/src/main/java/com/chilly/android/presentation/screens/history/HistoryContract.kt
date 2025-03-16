package com.chilly.android.presentation.screens.history

import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.model.HistoryItem

data class HistoryState(
    val historyItems: List<HistoryItem> = emptyList()
)

data class HistoryUiState(
    val historyItems: List<HistoryListItem> = emptyList()
)

sealed interface HistoryListItem {
    @JvmInline
    value class PlaceItem(val value: PlaceDto) : HistoryListItem

    @JvmInline
    value class HistoryDateLabel(val value: String) : HistoryListItem
}

sealed interface HistoryEvent {
    sealed interface UiEvent : HistoryEvent {
        data class ItemClicked(val placeId: Int): UiEvent
    }

    sealed interface CommandEvent : HistoryEvent {
        data class HistoryLoaded(val items: List<HistoryItem>) : CommandEvent
    }
}

sealed interface HistoryCommand {
    data object LoadHistory : HistoryCommand
}

sealed interface HistoryNews {
    data class NavigatePlaceInfo(val id: Int) : HistoryNews
}