package com.chilly.android.presentation.screens.history

import com.chilly.android.domain.model.HistoryItem

data class HistoryState(
    val historyItems: List<HistoryItem> = emptyList()
)

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