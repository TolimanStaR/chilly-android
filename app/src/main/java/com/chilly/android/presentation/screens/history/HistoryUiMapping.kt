package com.chilly.android.presentation.screens.history

import android.content.res.Resources
import com.chilly.android.di.screens.HistoryScope
import com.chilly.android.domain.model.HistoryItem
import com.chilly.android.presentation.common.logic.formattedDate
import com.chilly.android.presentation.common.structure.UiStateMapper
import java.time.LocalDateTime
import javax.inject.Inject

@HistoryScope
class HistoryUiMapper @Inject constructor() : UiStateMapper<HistoryState, HistoryUiState> {
    override suspend fun Resources.mapToUiState(state: HistoryState): HistoryUiState = HistoryUiState(
        historyItems = mapHistoryList(state.historyItems),
        showDeleteDialog = state.showDeleteDialog
    )

}

private fun Resources.mapHistoryList(items: List<HistoryItem>): List<HistoryListItem> {
    if (items.isEmpty()) return emptyList()
    return buildList {
        var lastLabel: HistoryListItem.HistoryDateLabel? = null
        items.forEach { item ->
            val label = mapToString(item.timestamp)
            if (label != lastLabel) {
                add(label)
                lastLabel = label
            }
            add(HistoryListItem.PlaceItem(item))
        }
    }
}

private fun Resources.mapToString(date: LocalDateTime): HistoryListItem.HistoryDateLabel {
    return HistoryListItem.HistoryDateLabel(formattedDate(date))
}