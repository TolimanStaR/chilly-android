package com.chilly.android.presentation.screens.forgotPassword

data class ForgotPasswordState(
    val emailText: String = "",
    val emailErrorResId: Int? = null,
    val sendButtonEnabled: Boolean = false,
    val step: RecoveryStep = RecoveryStep.EMAIL,
    val codeText: String = "",
    val isLoading: Boolean = false,
    val passwordText: String = "",
    val passwordErrorResId: Int? = null,
    val passwordShown: Boolean = false,
    val passwordRepeatText: String = "",
    val repeatPasswordErrorResId: Int? = null,
    val passwordRepeatShown: Boolean = false,
)

enum class RecoveryStep {
    EMAIL,
    CODE,
    NEW_PASSWORD
}

sealed interface ForgotPasswordEvent {
    sealed interface UiEvent : ForgotPasswordEvent {
        data class EmailTextChanged(val newValue: String) : UiEvent
        data object EmailTextCleared : UiEvent
        data object MainButtonClicked : UiEvent
        data object SignUpClicked : UiEvent
        data class CodeTextChanged(val newValue: String) : UiEvent
        data object CodeTextCleared : UiEvent
        class PasswordTextChanged(val newValue: String) : UiEvent
        data object PasswordVisibilityToggled : UiEvent
        class PasswordRepeatChanged(val newValue: String) : UiEvent
        data object PasswordRepeatVisibilityToggled : UiEvent
        data object BackPressed : UiEvent
    }

    sealed interface CommandEvent : ForgotPasswordEvent {
        data object EmailRejected : CommandEvent
        data object CodeSent : CommandEvent
        data object VerifiedSuccessfully : CommandEvent
        data object WrongCode : CommandEvent
        data object PasswordChanged : CommandEvent
        data object CommandFail : CommandEvent
    }
}

sealed interface ForgotPasswordCommand {
    data class SendCodeFor(val email: String) : ForgotPasswordCommand
    data class VerifyCode(val email: String, val code: String) : ForgotPasswordCommand
    data class ChangePassword(val email: String, val code: String, val password: String) : ForgotPasswordCommand
}

sealed interface ForgotPasswordNews {
    data object NavigateSignUp : ForgotPasswordNews
    data object NavigateLogin : ForgotPasswordNews
    data object FailedRequest : ForgotPasswordNews
    data object WrongEmail : ForgotPasswordNews
    data object PasswordChanged : ForgotPasswordNews
    data object WrongCode : ForgotPasswordNews
}