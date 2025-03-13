package com.chilly.android.presentation.screens.result


import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class RecommendationResultStore @Inject constructor(
    commandFlowHandler: RecommendationResultCommandFlowHandler,
    update: RecommendationResultUpdate
) : Store<RecommendationResultState, RecommendationResultEvent, RecommendationResultNews> by KoteaStore(
    initialState = RecommendationResultState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)