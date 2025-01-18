package com.chilly.android.presentation.login

sealed interface LoginEvent {

    sealed interface UiEvent : LoginEvent {
        class LoginChanged(val newValue: String) : UiEvent
        class PasswordChanged(val newValue: String) : UiEvent
        data object LogInClicked : UiEvent
        data object SignUpClicked : UiEvent
        data object ClearClicked : UiEvent
        data object ShowPasswordToggled : UiEvent
    }

    sealed interface CommandEvent : LoginEvent {
        data object LoginFail : CommandEvent
        class LoginSuccess(val refreshToken: String, val accessToken: String) : CommandEvent
    }
}