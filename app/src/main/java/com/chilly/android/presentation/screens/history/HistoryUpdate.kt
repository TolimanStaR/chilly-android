package com.chilly.android.presentation.screens.history

import com.chilly.android.presentation.screens.history.HistoryEvent.CommandEvent
import com.chilly.android.presentation.screens.history.HistoryEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class HistoryUpdate @Inject constructor(

) : DslUpdate<HistoryState, HistoryEvent, HistoryCommand, HistoryNews>() {

    override fun NextBuilder.update(event: HistoryEvent) = when (event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when (event) {
            is UiEvent.ItemClicked -> {
                news(HistoryNews.NavigatePlaceInfo(event.placeId))
            }
            UiEvent.ClearAllConfirmed -> {
                commands(HistoryCommand.ClearHistory)
            }
            is UiEvent.ItemSwipedToDelete -> {
                commands(HistoryCommand.DeleteItem(event.historyItem))
            }
            UiEvent.ProfileClicked -> {
                news(HistoryNews.NavigateProfile)
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            is CommandEvent.HistoryLoaded -> {
                state { copy(historyItems = event.items) }
            }
        }
    }
}
