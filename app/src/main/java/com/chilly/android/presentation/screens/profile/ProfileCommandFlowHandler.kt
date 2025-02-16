package com.chilly.android.presentation.screens.profile

import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class ProfileCommandFlowHandler @Inject constructor(

) : CommandsFlowHandler<ProfileCommand, ProfileEvent.CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<ProfileCommand>): Flow<ProfileEvent.CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is ProfileCommand.Load -> handle(it)
                else -> emptyFlow()
            }
        }

    private fun handle(command: ProfileCommand.Load): Flow<ProfileEvent.CommandEvent> = flow {
        // TODO()
    }
}