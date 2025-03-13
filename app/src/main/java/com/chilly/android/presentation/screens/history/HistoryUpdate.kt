package com.chilly.android.presentation.screens.history

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class HistoryUpdate @Inject constructor(

) : DslUpdate<HistoryState, HistoryEvent, HistoryCommand, HistoryNews>() {

    override fun NextBuilder.update(event: HistoryEvent) = when (event) {
        is HistoryEvent.UiEvent -> updateOnUi(event)
        is HistoryEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: HistoryEvent.UiEvent) {
        when (event) {
            is HistoryEvent.UiEvent.ItemClicked -> {
                news(HistoryNews.NavigatePlaceInfo(event.placeId))
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: HistoryEvent.CommandEvent) {
        when (event) {
            is HistoryEvent.CommandEvent.HistoryLoaded -> {
                state { copy(historyItems = event.items) }
            }
        }
    }
}
