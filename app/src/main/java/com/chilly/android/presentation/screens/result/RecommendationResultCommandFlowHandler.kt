package com.chilly.android.presentation.screens.result

import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.useCase.recommendation.GetRecommendationUseCase
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class RecommendationResultCommandFlowHandler @Inject constructor(
    private val getRecommendationUseCase: GetRecommendationUseCase,
    private val preferencesRepository: PreferencesRepository,
) : CommandsFlowHandler<RecommendationResultCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<RecommendationResultCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is RecommendationResultCommand.LoadRecommendations -> handleLoad()
                RecommendationResultCommand.CheckRequested -> handleCheck()
            }
        }

    private fun handleLoad(): Flow<CommandEvent> =
        flow {
            val event = getRecommendationUseCase.invoke()
                .map { CommandEvent.LoadingSuccess(it) }
                .getOrDefault(CommandEvent.LoadingFail)

            emit(event)
        }

    private fun handleCheck(): Flow<CommandEvent> =
        flow {
            if (preferencesRepository.hasRequestedRecommendation()) {
                emit(CommandEvent.ClearData)
            }
        }
}