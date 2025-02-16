package com.chilly.android.presentation.screens.main

import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject


class MainStore @Inject constructor(
    commandFlowHandler: MainCommandFlowHandler,
    update: MainUpdate
) : Store<Unit, MainEvent, MainNews> by KoteaStore(
    initialState = Unit,
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)