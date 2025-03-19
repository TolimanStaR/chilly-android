package com.chilly.android.presentation.screens.history

import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.presentation.screens.history.HistoryEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class HistoryCommandFlowHandler @Inject constructor(
    private val placeRepository: PlaceRepository
) : CommandsFlowHandler<HistoryCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<HistoryCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is HistoryCommand.LoadHistory -> handleLoad()
                HistoryCommand.ClearHistory -> handleClear()
                is HistoryCommand.DeleteItem -> handleRemoveItem(it)
            }
        }

    private fun handleLoad(): Flow<CommandEvent> =
        placeRepository.getHistoryFlow()
            .map { history ->
                CommandEvent.HistoryLoaded(history)
            }

    private fun handleClear(): Flow<CommandEvent> = flow {
        placeRepository.clearHistory()
    }

    private fun handleRemoveItem(command: HistoryCommand.DeleteItem): Flow<CommandEvent> = flow {
        placeRepository.removeItem(command.historyItem)
    }
}