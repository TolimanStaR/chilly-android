package com.chilly.android.presentation.screens.favorites

import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.presentation.screens.favorites.FavoritesEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.emptyFlow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class FavoritesCommandFlowHandler @Inject constructor(
    private val placeRepository: PlaceRepository
) : CommandsFlowHandler<FavoritesCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<FavoritesCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is FavoritesCommand.LoadFavorites -> handleLoad()
            }
        }

    private fun handleLoad(): Flow<CommandEvent> =
        placeRepository.getFavoritesFlow()
            .map { favorites ->
                CommandEvent.FavoritesLoaded(favorites)
            }
}