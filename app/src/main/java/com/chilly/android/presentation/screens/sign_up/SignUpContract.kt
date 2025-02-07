package com.chilly.android.presentation.screens.sign_up

data class SignUpState(
    val nameText: String = "",
    val emailText: String = "",
    val emailErrorRedId: Int? = null,
    val phoneText: String = "",
    val phoneErrorRedId: Int? = null,
    val passwordText: String = "",
    val passwordErrorRedId: Int? = null,
    val passwordShown: Boolean = false,
    val passwordRepeatText: String = "",
    val repeatPasswordErrorRedId: Int? = null,
    val passwordRepeatShown: Boolean = false,
    val isLoading: Boolean = false,
    val signUpEnabled: Boolean = false
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
        data class SignUpSuccess(
            val username: String,
            val password: String
        ) : CommandEvent
        data object LoginSuccess : CommandEvent
        data object LoginFailure : CommandEvent
        data class SignUpReasonedFail(val reason: FailReason = FailReason.GeneralFail) : CommandEvent
    }
}

sealed interface SignUpNews {
    data object NavigateMain : SignUpNews
    data object NavigateToLogin : SignUpNews
    data class ShowFailedSnackbar(val reason: FailReason = FailReason.GeneralFail) : SignUpNews
}

sealed interface SignUpCommand {
    data class SignUp(
        val nameText: String,
        val emailText: String,
        val phoneText: String,
        val passwordText: String
    ) : SignUpCommand
    data class LogIn(val username: String, val password: String) : SignUpCommand
}

sealed interface FailReason {
    data object GeneralFail : FailReason
    data object DataConflict : FailReason
}