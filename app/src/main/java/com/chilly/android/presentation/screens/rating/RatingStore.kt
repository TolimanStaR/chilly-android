package com.chilly.android.presentation.screens.rating


import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store

class RatingStore @AssistedInject constructor(
    commandFlowHandler: RatingCommandFlowHandler,
    update: RatingUpdate,
    @Assisted recommendedIds: List<Int>
) : Store<RatingState, RatingEvent, RatingNews> by KoteaStore(
    initialState = RatingState(recommendedIds),
    initialCommands = listOf(RatingCommand.Load(recommendedIds)),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
) {

    @AssistedFactory
    interface Factory {
        fun create(recommendedIds: List<Int>): RatingStore
    }
}