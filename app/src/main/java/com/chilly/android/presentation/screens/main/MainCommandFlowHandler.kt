package com.chilly.android.presentation.screens.main

import com.chilly.android.domain.repository.FeedRepository
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.screens.main.MainEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class MainCommandFlowHandler @Inject constructor(
    private val preferencesRepository: PreferencesRepository,
    private val feedRepository: FeedRepository
) : CommandsFlowHandler<MainCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<MainCommand>): Flow<CommandEvent> = commands.flatMapMerge {
        when(it) {
            MainCommand.CheckMainQuiz -> handleQuizCompletionCheck()
            MainCommand.LoadFeed -> handleLoad()
            MainCommand.LoadNewFeedPage -> handleNewPage()
            MainCommand.RefreshFeed -> handleRefresh()
        }
    }

    private fun handleQuizCompletionCheck(): Flow<CommandEvent> = flow {
        val completed = preferencesRepository.hasCompletedMainQuiz()
        emit(CommandEvent.CheckQuizResult(completed))
    }

    private fun handleLoad(): Flow<CommandEvent> = feedRepository
        .getFeedFlow()
        .map {
            CommandEvent.FeedUpdated(it)
        }

    private fun handleNewPage(): Flow<CommandEvent> = flow {
        feedRepository.requestNextPage()
            .onFailure {
                emit(CommandEvent.FeedUpdateFailed)
            }
    }

    private fun handleRefresh(): Flow<CommandEvent> = flow {
        feedRepository.refreshFeed()
            .onFailure {
                emit(CommandEvent.FeedUpdateFailed)
            }
            .onSuccess { isInNewLocation ->
                if (!isInNewLocation) {
                    emit(CommandEvent.SameLocationRefresh)
                }
            }
    }
}