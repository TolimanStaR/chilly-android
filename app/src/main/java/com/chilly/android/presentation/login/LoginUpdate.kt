package com.chilly.android.presentation.login

import ru.tinkoff.kotea.core.dsl.DslUpdate

class LoginUpdate : DslUpdate<LoginState, LoginEvent, LoginCommand, LoginNews>() {

    override fun NextBuilder.update(event: LoginEvent) {
        when (event) {
            is LoginEvent.UiEvent -> onUiEvent(event)
            is LoginEvent.CommandEvent -> onCommandEvent(event)
        }
    }

    private fun NextBuilder.onUiEvent(event: LoginEvent.UiEvent) {
        when(event) {
            is LoginEvent.UiEvent.LoginChanged -> {
                state { copy(loginText = event.newValue) }
                checkLoginEnabled()
            }
            is LoginEvent.UiEvent.PasswordChanged -> {
                state { copy(passwordText = event.newValue) }
                checkLoginEnabled()
            }
            LoginEvent.UiEvent.LogInClicked -> {
                state { copy(isLoading = true) }
                commands(LoginCommand.LogIn(state.loginText, state.passwordText))
            }
            LoginEvent.UiEvent.ClearClicked -> state { copy(loginText = "", loginButtonEnabled = false) }
            LoginEvent.UiEvent.ShowPasswordToggled -> state { copy(passwordShown = !passwordShown) }
            LoginEvent.UiEvent.SignUpClicked -> news(LoginNews.NavigateSignUp)
            LoginEvent.UiEvent.ForgotPasswordClicked -> news(LoginNews.NavigateForgotPassword)
        }
    }

    private fun NextBuilder.onCommandEvent(event: LoginEvent.CommandEvent) {
        when(event) {
            LoginEvent.CommandEvent.LoginFail -> {
                state { copy(isLoading = false) }
                news(LoginNews.LoginFailed)
            }
            is LoginEvent.CommandEvent.LoginSuccess -> {
                state { copy(isLoading = false) }
                news(LoginNews.NavigateMain)
            }
        }
    }

    private fun NextBuilder.checkLoginEnabled() {
        state { copy(loginButtonEnabled = loginText.isNotBlank() && passwordText.isNotBlank()) }
    }

}