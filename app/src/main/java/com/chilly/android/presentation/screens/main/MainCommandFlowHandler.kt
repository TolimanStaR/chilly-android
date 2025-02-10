package com.chilly.android.presentation.screens.main

import com.chilly.android.presentation.screens.main.MainEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class MainCommandFlowHandler @Inject constructor(
    
) : CommandsFlowHandler<MainCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<MainCommand>): Flow<CommandEvent> = commands.flatMapMerge {
        when(it) {
            is MainCommand.Load -> handleLoad(it)
        }
    }

    private fun handleLoad(command: MainCommand.Load): Flow<CommandEvent> = flow {
        // TODO()
    }
}