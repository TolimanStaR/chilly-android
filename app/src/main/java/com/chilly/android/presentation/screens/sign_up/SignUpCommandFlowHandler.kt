package com.chilly.android.presentation.screens.sign_up

import com.chilly.android.data.remote.HandledException
import com.chilly.android.data.remote.api.LoginApi
import com.chilly.android.data.remote.dto.request.LoginRequest
import com.chilly.android.data.remote.dto.request.SignUpRequest
import com.chilly.android.domain.useCase.login.LogInUseCase
import com.chilly.android.presentation.screens.sign_up.SignUpEvent.CommandEvent
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class SignUpCommandFlowHandler @Inject constructor(
    private val loginApi: LoginApi,
    private val loginUseCase: LogInUseCase
) : CommandsFlowHandler<SignUpCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<SignUpCommand>): Flow<CommandEvent> =
        commands.flatMapMerge { command ->
            when(command) {
                is SignUpCommand.SignUp -> handleSignUp(command)
                is SignUpCommand.LogIn -> handleLogIn(command)
            }
        }

    private fun handleSignUp(command: SignUpCommand.SignUp): Flow<CommandEvent> = flow {
        with(command) {
            loginApi.signUp(SignUpRequest(phoneText, emailText, nameText, passwordText))
        }.onSuccess {
            emit(CommandEvent.SignUpSuccess(command.emailText, command.passwordText))
        }.onFailure { exception ->
            val event = when {
                exception.becauseOfTakenUsername() -> CommandEvent.SignUpReasonedFail(FailReason.DataConflict)
                else -> CommandEvent.SignUpReasonedFail()
            }
            emit(event)
        }
    }

    private fun handleLogIn(command: SignUpCommand.LogIn): Flow<CommandEvent> = flow {
        loginUseCase.invoke(LoginRequest(command.username, command.password))
            .onSuccess {
                emit(CommandEvent.LoginSuccess)
            }
            .onFailure {
                emit(CommandEvent.LoginFailure)
            }
    }

    private fun Throwable.becauseOfTakenUsername(): Boolean =
        this is HandledException && errorResponse.statusCode == HttpStatusCode.Conflict.value
}