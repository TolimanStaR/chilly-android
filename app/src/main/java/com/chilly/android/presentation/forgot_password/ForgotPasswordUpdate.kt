package com.chilly.android.presentation.forgot_password

import androidx.core.text.isDigitsOnly
import com.chilly.android.R
import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.presentation.common.logic.ValidationType
import com.chilly.android.presentation.forgot_password.ForgotPasswordEvent.CommandEvent
import com.chilly.android.presentation.forgot_password.ForgotPasswordEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject


private const val VERIFICATION_CODE_LENGTH = 6

class ForgotPasswordUpdate @Inject constructor(
    private val fieldValidator: FieldValidator
) : DslUpdate<ForgotPasswordState, ForgotPasswordEvent, ForgotPasswordCommand, ForgotPasswordNews>() {

    override fun NextBuilder.update(event: ForgotPasswordEvent) = when(event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when(event) {
            UiEvent.BackPressed -> {
                if (state.step == RecoveryStep.EMAIL) {
                    news(ForgotPasswordNews.NavigateLogin)
                } else {
                    state { copy(step = RecoveryStep.entries[step.ordinal - 1]) }
                }
            }
            is UiEvent.CodeTextChanged -> {
                state { copy(codeText = event.newValue) }
                checkMainButtonEnabled()
            }
            UiEvent.CodeTextCleared -> {
                state { copy(codeText = "", sendButtonEnabled = false) }
            }
            is UiEvent.EmailTextChanged -> {
                state { copy(emailText = event.newValue, emailErrorResId = null) }
                checkMainButtonEnabled()
            }
            UiEvent.EmailTextCleared -> {
                state { copy(emailText = "", sendButtonEnabled = false) }
            }
            UiEvent.MainButtonClicked -> handleMainButtonClick()
            is UiEvent.PasswordRepeatChanged -> {
                state { copy(passwordRepeatText = event.newValue, repeatPasswordErrorResId = null) }
                checkMainButtonEnabled()
            }
            UiEvent.PasswordRepeatVisibilityToggled -> {
                state { copy(passwordRepeatShown = !passwordRepeatShown) }
            }
            is UiEvent.PasswordTextChanged -> {
                state { copy(passwordText = event.newValue, passwordErrorResId = null) }
                checkMainButtonEnabled()
            }
            UiEvent.PasswordVisibilityToggled -> {
                state { copy(passwordShown = !passwordShown) }
            }
            UiEvent.SignUpClicked -> news(ForgotPasswordNews.NavigateSignUp)
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when(event) {
            CommandEvent.CodeSent -> {
                state { copy(isLoading = false, step = RecoveryStep.CODE, sendButtonEnabled = false) }
            }
            CommandEvent.EmailRejected -> {
                state { copy(isLoading = false, sendButtonEnabled = false) }
                news(ForgotPasswordNews.WrongEmail)
            }
            CommandEvent.CommandFail -> {
                state { copy(isLoading = false) }
                news(ForgotPasswordNews.FailedRequest)
            }
            CommandEvent.PasswordChanged -> {
                state { copy(isLoading = false) }
                news(ForgotPasswordNews.PasswordChanged, ForgotPasswordNews.NavigateLogin)
            }
            CommandEvent.VerifiedSuccessfully ->  {
                state { copy(isLoading = false, step = RecoveryStep.NEW_PASSWORD, sendButtonEnabled = false) }
            }
            CommandEvent.WrongCode -> {
                state { copy(isLoading = false) }
                news(ForgotPasswordNews.WrongCode)
            }
        }
    }

    private fun NextBuilder.checkMainButtonEnabled() {
        val enabled = with(state) {
            when(state.step) {
                RecoveryStep.EMAIL -> emailText.isNotBlank()
                RecoveryStep.CODE -> codeText.isDigitsOnly() && codeText.length == VERIFICATION_CODE_LENGTH
                RecoveryStep.NEW_PASSWORD -> passwordText.isNotBlank() &&
                        passwordRepeatText.isNotBlank()
            }
        }
        state { copy(sendButtonEnabled = enabled) }
    }

    private fun NextBuilder.validateFields() {
        state {
            when(step) {
                RecoveryStep.EMAIL -> copy(
                    emailErrorResId = fieldValidator.checkError(emailText, ValidationType.Email),
                )
                RecoveryStep.CODE -> this@state
                RecoveryStep.NEW_PASSWORD -> copy(
                    passwordErrorResId = fieldValidator.checkError(passwordText, ValidationType.Password),
                    repeatPasswordErrorResId = if (passwordText != passwordRepeatText) R.string.passwords_differ else null
                )
            }
        }

        state {
            copy(
                sendButtonEnabled = listOf(emailErrorResId, passwordErrorResId, repeatPasswordErrorResId)
                    .all { it == null }
            )
        }
    }

    private fun NextBuilder.handleMainButtonClick() {
        validateFields()
        if (!state.sendButtonEnabled) {
            return
        }
        with(state) {
            when(step) {
                RecoveryStep.EMAIL -> commands(ForgotPasswordCommand.SendCodeFor(emailText))
                RecoveryStep.CODE -> commands(ForgotPasswordCommand.VerifyCode(emailText, codeText))
                RecoveryStep.NEW_PASSWORD -> commands(ForgotPasswordCommand.ChangePassword(emailText, codeText, passwordText))
            }
        }
        state { copy(isLoading = true) }
    }

}