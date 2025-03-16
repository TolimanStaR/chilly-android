package com.chilly.android.presentation.screens.history

import android.content.res.Resources
import com.chilly.android.R
import com.chilly.android.domain.model.HistoryItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.DAYS
import java.util.Locale

suspend fun Resources.mapToHistoryUi(state: HistoryState): HistoryUiState = withContext(Dispatchers.Default) {
    HistoryUiState(historyItems = mapHistoryList(state.historyItems))
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
            add(HistoryListItem.PlaceItem(item.place))
        }
    }
}

private fun Resources.mapToString(date: LocalDateTime): HistoryListItem.HistoryDateLabel {
    val now = LocalDateTime.now()
    val dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy", Locale.getDefault())
    val timeFormatter = DateTimeFormatter.ofPattern("HH:mm", Locale.getDefault())

    val dayStr = when {
        now.truncatedTo(DAYS) == date.truncatedTo(DAYS) -> getString(R.string.today)
        now.truncatedTo(DAYS).minusDays(1) == date.truncatedTo(DAYS) -> getString(R.string.yesterday)
        else -> date.format(dateFormatter)
    }
    val timeStr = date.format(timeFormatter)

    return HistoryListItem.HistoryDateLabel("$dayStr, $timeStr")
}