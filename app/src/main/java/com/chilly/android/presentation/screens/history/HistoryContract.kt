package com.chilly.android.presentation.screens.history

import com.chilly.android.domain.model.HistoryItem

data class HistoryState(
    val historyItems: List<HistoryItem> = emptyList()
)

data class HistoryUiState(
    val historyItems: List<HistoryListItem> = emptyList()
)

sealed interface HistoryListItem {
    @JvmInline
    value class PlaceItem(val item: HistoryItem) : HistoryListItem

    @JvmInline
    value class HistoryDateLabel(val value: String) : HistoryListItem
}

sealed interface HistoryEvent {
    sealed interface UiEvent : HistoryEvent {
        data class ItemClicked(val placeId: Int): UiEvent
        data object ProfileClicked : UiEvent
        data object ClearAllConfirmed : UiEvent
        data class ItemSwipedToDelete(val historyItem: HistoryItem) : UiEvent
    }

    sealed interface CommandEvent : HistoryEvent {
        data class HistoryLoaded(val items: List<HistoryItem>) : CommandEvent
    }
}

sealed interface HistoryCommand {
    data object LoadHistory : HistoryCommand
    data object ClearHistory : HistoryCommand
    data class DeleteItem(val historyItem: HistoryItem) : HistoryCommand
}

sealed interface HistoryNews {
    data class NavigatePlaceInfo(val id: Int) : HistoryNews
    data object NavigateProfile : HistoryNews
}