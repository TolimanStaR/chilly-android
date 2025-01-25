package com.chilly.android.presentation.sign_up

data class SignUpState(
    val nameText: String = "",
    val emailText: String = "",
    val phoneText: String = "",
    val passwordText: String = "",
    val passwordShown: Boolean = false,
    val passwordRepeatText: String = "",
    val passwordRepeatShown: Boolean = false,
    val isLoading: Boolean = false
)

sealed interface SignUpEvent {
    sealed interface UiEvent : SignUpEvent {
        class NameTextChanged(val newValue: String) : UiEvent
        data object ClearNameClicked : UiEvent
        class EmailTextChanged(val newValue: String) : UiEvent
        data object ClearEmailClicked : UiEvent
        class PhoneTextChanged(val newValue: String) : UiEvent
        data object ClearPhoneClicked : UiEvent
        class PasswordTextChanged(val newValue: String) : UiEvent
        data object PasswordVisibilityToggled : UiEvent
        class PasswordRepeatChanged(val newValue: String) : UiEvent
        data object PasswordRepeatVisibilityToggled : UiEvent
        data object SignUpClicked : UiEvent
        data object HaveAccountClicked : UiEvent
    }

    sealed interface CommandEvent : SignUpEvent {
        data object SignUpSuccess : CommandEvent
        data object SignUpFailed : CommandEvent
    }
}

sealed interface SignUpNews {
    data object NavigateMain : SignUpNews
    data object NavigateToLogin : SignUpNews
    data object ShowFailedSnackbar : SignUpNews
}

sealed interface SignUpCommand {
    data class SignUp(
        val nameText: String,
        val emailText: String,
        val phoneText: String,
        val passwordText: String
    ) : SignUpCommand
}