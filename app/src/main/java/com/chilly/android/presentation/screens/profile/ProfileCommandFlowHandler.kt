package com.chilly.android.presentation.screens.profile

import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.UserRepository
import com.chilly.android.presentation.screens.profile.ProfileEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import timber.log.Timber
import javax.inject.Inject

class ProfileCommandFlowHandler @Inject constructor(
    private val userRepository: UserRepository,
    private val preferencesRepository: PreferencesRepository
) : CommandsFlowHandler<ProfileCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<ProfileCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is ProfileCommand.LoadLoggedUser -> handleUserLoad()
                ProfileCommand.LogOut -> handleLogOut()
                ProfileCommand.ClearInterests -> handleClearInterests()
            }
        }

    private fun handleLogOut(): Flow<CommandEvent> = flow {
        preferencesRepository.saveRefreshToken(null)
    }

    private fun handleClearInterests(): Flow<CommandEvent> = flow {
        preferencesRepository.setHasCompletedMainQuiz(false)
        emit(CommandEvent.InterestsCleared)
    }

    private fun handleUserLoad(): Flow<CommandEvent> = flow {
        val event = runCatching { userRepository.getLoggedUser() }
            .onFailure { Timber.e(it) }
            .map(CommandEvent::UserLoaded)
            .getOrDefault(CommandEvent.Fail)
        emit(event)
    }
}