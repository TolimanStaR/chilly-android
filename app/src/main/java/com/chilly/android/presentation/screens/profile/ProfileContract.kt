package com.chilly.android.presentation.screens.profile

import com.chilly.android.data.remote.dto.UserDto

// when user is null, screen needs to show loading process
data class ProfileState(
    val user: UserDto? = null,
    val nameText: String = "",
    val emailText: String = "",
    val phoneText: String = "",
    val changeDataEnabled: Boolean = false,
    val oldPasswordText: String = "",
    val newPasswordText: String = "",
    val repeatPasswordText: String = "",
    val changePasswordEnabled: Boolean = false,
    val oldPasswordShown: Boolean = false,
    val newPasswordShown: Boolean = false,
    val repeatPasswordShown: Boolean = false,
)

sealed interface ProfileEvent {
    sealed interface UiEvent : ProfileEvent {
        data class NameTextChanged(val newValue: String) : UiEvent
        data class EmailTextChanged(val newValue: String) : UiEvent
        data class PhoneTextChanged(val newValue: String) : UiEvent
        data class OldPasswordChanged(val newValue: String) : UiEvent
        data class NewPasswordChanged(val newValue: String) : UiEvent
        data class RepeatPasswordChanged(val newValue: String) : UiEvent
        data object OldPasswordVisibilityToggled : UiEvent
        data object NewPasswordVisibilityToggled : UiEvent
        data object RepeatPasswordVisibilityToggled : UiEvent
        data object LogOutClicked : UiEvent
        data object ShowOnboardingClicked : UiEvent
        data object ChangeDataClicked : UiEvent
        data object ChangePasswordClicked : UiEvent
        data object ShownLoadingScreen : UiEvent
    }

    sealed interface CommandEvent : ProfileEvent {
        data class UserLoaded(val user: UserDto) : CommandEvent
        data object Fail : CommandEvent
    }
}

sealed interface ProfileCommand {
    data object LoadLoggedUser : ProfileCommand
    data object LogOut : ProfileCommand
}

sealed interface ProfileNews {
    data object GeneralFail : ProfileNews
    data object NavigateSignIn : ProfileNews
    data object NavigateOnboarding : ProfileNews
    data object NavigateBack : ProfileNews
}