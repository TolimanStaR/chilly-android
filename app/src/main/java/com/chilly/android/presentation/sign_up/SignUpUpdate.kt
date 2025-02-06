package com.chilly.android.presentation.sign_up

import com.chilly.android.R
import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.presentation.common.logic.ValidationType
import com.chilly.android.presentation.sign_up.SignUpEvent.CommandEvent
import com.chilly.android.presentation.sign_up.SignUpEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class SignUpUpdate @Inject constructor(
    private val fieldValidator: FieldValidator
) : DslUpdate<SignUpState, SignUpEvent, SignUpCommand, SignUpNews>() {

    override fun NextBuilder.update(event: SignUpEvent) {
        when(event) {
            is UiEvent -> updateOnUi(event)
            is CommandEvent -> updateOnCommand(event)
        }
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when(event) {
            UiEvent.ClearEmailClicked -> state { copy(emailText = "", signUpEnabled = false) }
            UiEvent.ClearNameClicked -> state { copy(nameText = "", signUpEnabled = false) }
            UiEvent.ClearPhoneClicked -> state { copy(phoneText = "", signUpEnabled = false) }
            is UiEvent.EmailTextChanged -> {
                state { copy(emailText = event.newValue, emailErrorRedId = null) }
                checkSignUpEnabled()
            }
            UiEvent.HaveAccountClicked -> news(SignUpNews.NavigateToLogin)
            is UiEvent.NameTextChanged -> {
                state { copy(nameText = event.newValue) }
                checkSignUpEnabled()
            }
            is UiEvent.PasswordRepeatChanged -> {
                state { copy(passwordRepeatText = event.newValue, repeatPasswordErrorRedId = null) }
                checkSignUpEnabled()
            }
            UiEvent.PasswordRepeatVisibilityToggled -> state {
                copy(passwordRepeatShown = !passwordRepeatShown)
            }
            is UiEvent.PasswordTextChanged -> {
                state { copy(passwordText = event.newValue, passwordErrorRedId = null) }
                checkSignUpEnabled()
            }
            UiEvent.PasswordVisibilityToggled -> state {
                copy(passwordShown = !passwordShown)
            }
            is UiEvent.PhoneTextChanged -> {
                state { copy(phoneText = event.newValue, phoneErrorRedId = null) }
                checkSignUpEnabled()
            }
            UiEvent.SignUpClicked -> {
                validateFields()
                if (state.signUpEnabled) {
                    with(state) {
                        commands(SignUpCommand.SignUp(nameText, emailText, phoneText, passwordText))
                    }
                    state { copy(isLoading = true) }
                }
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when(event) {
            is CommandEvent.SignUpSuccess -> {
                commands(SignUpCommand.LogIn(event.username, event.password))
            }
            is CommandEvent.SignUpReasonedFail -> {
                state { copy(isLoading = false) }
                news(SignUpNews.ShowFailedSnackbar(event.reason))
            }
            CommandEvent.LoginFailure -> {
                state { copy(isLoading = false) }
                news(SignUpNews.ShowFailedSnackbar())
            }
            CommandEvent.LoginSuccess -> {
                state { copy(isLoading = false) }
                news(SignUpNews.NavigateMain)
            }
        }
    }

    private fun NextBuilder.checkSignUpEnabled() {
        state {
            copy(
                signUpEnabled = listOf(nameText, emailText, phoneText, passwordText, passwordRepeatText)
                    .all { it.isNotBlank() }
            )
        }
    }

    private fun NextBuilder.validateFields() {
        state {
            copy(
                emailErrorRedId = fieldValidator.checkError(emailText, ValidationType.Email),
                phoneErrorRedId = fieldValidator.checkError(phoneText, ValidationType.Phone),
                passwordErrorRedId = fieldValidator.checkError(passwordText, ValidationType.Password),
                repeatPasswordErrorRedId = if (passwordText != passwordRepeatText) R.string.passwords_differ else null
            )
        }

        state {
            copy(
                signUpEnabled = listOf(emailErrorRedId, phoneErrorRedId, passwordErrorRedId, repeatPasswordErrorRedId)
                    .all { it == null }
            )
        }
    }
}