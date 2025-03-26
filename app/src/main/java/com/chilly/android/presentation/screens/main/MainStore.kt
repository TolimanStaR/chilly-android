package com.chilly.android.presentation.screens.main

import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject


class MainStore @Inject constructor(
    commandFlowHandler: MainCommandFlowHandler,
    update: MainUpdate
) : Store<MainState, MainEvent, MainNews> by KoteaStore(
    initialState = MainState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)