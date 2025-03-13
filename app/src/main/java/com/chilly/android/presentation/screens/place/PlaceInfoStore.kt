package com.chilly.android.presentation.screens.place


import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store

class PlaceInfoStore @AssistedInject constructor(
    commandFlowHandler: PlaceInfoCommandFlowHandler,
    update: PlaceInfoUpdate,
    @Assisted placeId: Int
) : Store<PlaceInfoState, PlaceInfoEvent, PlaceInfoNews> by KoteaStore(
    initialState = PlaceInfoState(placeId),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
) {

    @AssistedFactory
    interface Factory {
        fun create(placeId: Int): PlaceInfoStore
    }
}