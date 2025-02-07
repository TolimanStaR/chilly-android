package com.chilly.android.presentation.screens.login

sealed interface LoginCommand {
    data class LogIn(val username: String, val password: String) : LoginCommand
}

sealed interface LoginEvent {

    sealed interface UiEvent : LoginEvent {
        class LoginChanged(val newValue: String) : UiEvent
        class PasswordChanged(val newValue: String) : UiEvent
        data object LogInClicked : UiEvent
        data object SignUpClicked : UiEvent
        data object ClearClicked : UiEvent
        data object ShowPasswordToggled : UiEvent
        data object ForgotPasswordClicked : UiEvent
    }

    sealed interface CommandEvent : LoginEvent {
        data object LoginFail : CommandEvent
        data class LoginSuccess(val refreshToken: String, val accessToken: String) : CommandEvent
    }
}

sealed interface LoginNews {
    data object NavigateMain : LoginNews
    data object NavigateSignUp : LoginNews
    data object LoginFailed : LoginNews
    data object NavigateForgotPassword : LoginNews
}

data class LoginState(
    val loginText: String = "",
    val passwordText: String = "",
    val loginButtonEnabled: Boolean = false,
    val passwordShown: Boolean = false,
    val isLoading: Boolean = false
)