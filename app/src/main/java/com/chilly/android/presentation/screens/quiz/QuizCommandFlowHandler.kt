package com.chilly.android.presentation.screens.quiz

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class QuizCommandFlowHandler @Inject constructor(

) : CommandsFlowHandler<QuizCommand, QuizEvent.CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<QuizCommand>): Flow<QuizEvent.CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is QuizCommand.Load -> handle(it)
                else -> emptyFlow()
            }
        }

    private fun handle(command: QuizCommand.Load): Flow<QuizEvent.CommandEvent> = flow {
        // TODO()
    }
}