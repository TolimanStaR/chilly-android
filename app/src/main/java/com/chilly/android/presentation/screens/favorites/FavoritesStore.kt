package com.chilly.android.presentation.screens.favorites


import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class FavoritesStore @Inject constructor(
    commandFlowHandler: FavoritesCommandFlowHandler,
    update: FavoritesUpdate
) : Store<FavoritesState, FavoritesEvent, FavoritesNews> by KoteaStore(
    initialState = FavoritesState(),
    initialCommands = listOf(FavoritesCommand.LoadFavorites),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)