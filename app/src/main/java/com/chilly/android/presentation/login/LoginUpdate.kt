package com.chilly.android.presentation.login

import ru.tinkoff.kotea.core.dsl.DslUpdate
import timber.log.Timber

class LoginUpdate : DslUpdate<LoginState, LoginEvent, LoginCommand, LoginNews>() {

    override fun NextBuilder.update(event: LoginEvent) {
        when (event) {
            is LoginEvent.UiEvent -> onUiEvent(event)
            is LoginEvent.CommandEvent -> onCommandEvent(event)
        }
    }

    private fun NextBuilder.onUiEvent(event: LoginEvent.UiEvent) {
        when(event) {
            is LoginEvent.UiEvent.LoginChanged -> state { copy(loginText = event.newValue) }
            is LoginEvent.UiEvent.PasswordChanged -> state { copy(passwordText = event.newValue) }
            LoginEvent.UiEvent.LogInClicked -> commands(LoginCommand.LogIn(state.loginText, state.passwordText))
        }
    }

    private fun NextBuilder.onCommandEvent(event: LoginEvent.CommandEvent) {
        when(event) {
            LoginEvent.CommandEvent.LoginFail -> Timber.e("login failed")
            is LoginEvent.CommandEvent.LoginSuccess -> Timber.i("login success token is ${event.accessToken}")
        }
    }
}