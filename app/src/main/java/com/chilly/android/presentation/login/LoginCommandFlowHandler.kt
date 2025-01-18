package com.chilly.android.presentation.login

import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.presentation.login.LoginEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class LoginCommandFlowHandler @Inject constructor(
    private val loginApi: LoginApi,
    private val preferencesRepo: PreferencesRepository
) : CommandsFlowHandler<LoginCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<LoginCommand>): Flow<CommandEvent> {
        return commands.flatMapMerge { command ->
            when(command) {
                is LoginCommand.LogIn -> handleLogIn(command)
            }
        }
    }

    private fun handleLogIn(command: LoginCommand.LogIn): Flow<CommandEvent> = flow {
        val response = loginApi.login(LoginRequest(command.username, command.password))
            .getOrElse {
                emit(CommandEvent.LoginFail)
                return@flow
            }
        preferencesRepo.saveRefreshToken(response.refreshToken)
        emit(CommandEvent.LoginSuccess(response.refreshToken, response.accessToken))
    }
}