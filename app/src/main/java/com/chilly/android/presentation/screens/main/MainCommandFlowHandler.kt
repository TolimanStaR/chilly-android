package com.chilly.android.presentation.screens.main

import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.screens.main.MainEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class MainCommandFlowHandler @Inject constructor(
    private val preferencesRepository: PreferencesRepository
) : CommandsFlowHandler<MainCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<MainCommand>): Flow<CommandEvent> = commands.flatMapMerge {
        when(it) {
            MainCommand.CheckMainQuiz -> handleQuizCompletionCheck()
        }
    }

    private fun handleQuizCompletionCheck(): Flow<CommandEvent> = flow {
        val completed = preferencesRepository.hasCompletedMainQuiz()
        emit(CommandEvent.CheckQuizResult(completed))
    }
}