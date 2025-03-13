package com.chilly.android.presentation.screens.history


import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class HistoryStore @Inject constructor(
    commandFlowHandler: HistoryCommandFlowHandler,
    update: HistoryUpdate
) : Store<HistoryState, HistoryEvent, HistoryNews> by KoteaStore(
    initialState = HistoryState(),
    initialCommands = listOf(HistoryCommand.LoadHistory),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)