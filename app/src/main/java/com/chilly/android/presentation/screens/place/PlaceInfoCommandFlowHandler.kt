package com.chilly.android.presentation.screens.place

import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.presentation.screens.place.PlaceInfoEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class PlaceInfoCommandFlowHandler @Inject constructor(
    private val placeRepository: PlaceRepository
) : CommandsFlowHandler<PlaceInfoCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<PlaceInfoCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is PlaceInfoCommand.LoadPlace -> handle(it)
            }
        }

    private fun handle(command: PlaceInfoCommand.LoadPlace): Flow<CommandEvent> = flow {
        val event = placeRepository.placeById(command.id)
            .map { CommandEvent.LoadSuccess(it) }
            .getOrDefault(CommandEvent.LoadFail)

        emit(event)
    }
}