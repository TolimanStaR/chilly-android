package com.chilly.android.presentation.screens.quiz


import ru.tinkoff.kotea.core.KoteaStore
import ru.tinkoff.kotea.core.Store
import javax.inject.Inject

class QuizStore @Inject constructor(
    commandFlowHandler: QuizCommandFlowHandler,
    update: QuizUpdate
) : Store<QuizState, QuizEvent, QuizNews> by KoteaStore(
    initialState = QuizState(),
    commandsFlowHandlers = listOf(commandFlowHandler),
    update = update
)