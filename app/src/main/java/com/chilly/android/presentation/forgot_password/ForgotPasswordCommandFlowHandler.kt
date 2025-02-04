package com.chilly.android.presentation.forgot_password

import com.chilly.android.data.remote.api.PasswordRecoveryApi
import com.chilly.android.data.remote.dto.request.RecoverPasswordRequest
import com.chilly.android.data.remote.dto.request.SendCodeRequest
import com.chilly.android.data.remote.dto.request.VerifyCodeRequest
import com.chilly.android.presentation.forgot_password.ForgotPasswordEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class ForgotPasswordCommandFlowHandler @Inject constructor(
    private val recoveryApi: PasswordRecoveryApi
) : CommandsFlowHandler<ForgotPasswordCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<ForgotPasswordCommand>): Flow<CommandEvent> = commands.flatMapMerge {
        when(it) {
            is ForgotPasswordCommand.SendCodeFor -> sendCode(it)
            is ForgotPasswordCommand.VerifyCode -> verifyCode(it)
            is ForgotPasswordCommand.ChangePassword -> changePassword(it)
        }
    }

    private fun sendCode(command: ForgotPasswordCommand.SendCodeFor): Flow<CommandEvent> = flow {
        // emit EmailRejected, GeneralFail, SendSuccess
        val event = recoveryApi.sendCode(SendCodeRequest(command.email))
            .map { CommandEvent.CodeSent }
            .getOrDefault(CommandEvent.CommandFail)
        emit(event)
    }

    private fun verifyCode(command: ForgotPasswordCommand.VerifyCode): Flow<CommandEvent> = flow {
        // emit WrongCode, VerifySuccess, GeneralFail
        val event = recoveryApi.verifyCode(VerifyCodeRequest(command.email, command.code))
            .map { response ->
                if (response.verified)
                    CommandEvent.VerifiedSuccessfully
                else
                    CommandEvent.WrongCode
            }
            .getOrDefault(CommandEvent.CommandFail)
        emit(event)
    }

    private fun changePassword(command: ForgotPasswordCommand.ChangePassword): Flow<CommandEvent> = flow {
        val event = recoveryApi.recoverPassword(RecoverPasswordRequest(command.email, command.code, command.password))
            .map { CommandEvent.PasswordChanged }
            .getOrDefault(CommandEvent.CommandFail)
        emit(event)
    }
}