package com.chilly.android.presentation.screens.profile

import com.chilly.android.R
import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.presentation.common.logic.ValidationType
import com.chilly.android.presentation.screens.profile.ProfileEvent.CommandEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class ProfileUpdate @Inject constructor(
    private val validator: FieldValidator
) : DslUpdate<ProfileState, ProfileEvent, ProfileCommand, ProfileNews>() {

    override fun NextBuilder.update(event: ProfileEvent) = when (event) {
        is ProfileEvent.UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: ProfileEvent.UiEvent) {
        when (event) {
            ProfileEvent.UiEvent.ChangeDataClicked -> handleDataChange()
            ProfileEvent.UiEvent.ChangePasswordClicked -> handlePasswordChange()
            is ProfileEvent.UiEvent.EmailTextChanged -> {
                state { copy(emailText = event.newValue, emailErrorTextRes = null) }
                checkChangeDataEnabled()
            }
            ProfileEvent.UiEvent.LogOutClicked -> {
                commands(ProfileCommand.LogOut)
                news(ProfileNews.NavigateSignIn)
            }
            is ProfileEvent.UiEvent.NameTextChanged -> {
                state { copy(nameText = event.newValue) }
                checkChangeDataEnabled()
            }
            is ProfileEvent.UiEvent.NewPasswordChanged -> {
                state { copy(newPasswordText = event.newValue) }
                checkChangePasswordEnabled()
            }
            ProfileEvent.UiEvent.NewPasswordVisibilityToggled -> {
                state { copy(newPasswordShown = !newPasswordShown) }
            }
            is ProfileEvent.UiEvent.OldPasswordChanged -> {
                state { copy(oldPasswordText = event.newValue)}
                checkChangePasswordEnabled()
            }
            ProfileEvent.UiEvent.OldPasswordVisibilityToggled -> {
                state { copy(oldPasswordShown = !oldPasswordShown) }
            }
            is ProfileEvent.UiEvent.PhoneTextChanged -> {
                state { copy(phoneText = event.newValue, phoneErrorTextRes = null)}
                checkChangeDataEnabled()
            }
            is ProfileEvent.UiEvent.RepeatPasswordChanged -> {
                state { copy(repeatPasswordText = event.newValue)}
                checkChangePasswordEnabled()
            }
            ProfileEvent.UiEvent.RepeatPasswordVisibilityToggled -> {
                state { copy(repeatPasswordShown = !repeatPasswordShown) }
            }
            ProfileEvent.UiEvent.ShowOnboardingClicked -> {
                news(ProfileNews.NavigateOnboarding)
            }

            ProfileEvent.UiEvent.ShownLoadingScreen -> {
                commands(ProfileCommand.LoadLoggedUser)
            }

            ProfileEvent.UiEvent.ClearInterestsClicked -> {
                commands(ProfileCommand.ClearInterests)
            }
        }
    }

    private fun NextBuilder.checkChangePasswordEnabled() {
        val enabled = with(state) {
            listOf(oldPasswordText, newPasswordText, repeatPasswordText)
                .all(String::isNotBlank)
        }
        state { copy(changePasswordEnabled = enabled) }
    }

    private fun NextBuilder.checkChangeDataEnabled() {
        val enabled = with(state) {
            listOf(nameText to user?.name, emailText to user?.email, phoneText to user?.phone)
                .any { (text, userValue) -> text.isNotBlank() && text != userValue }
        }
        state { copy(changeDataEnabled = enabled) }
    }

    private fun NextBuilder.handlePasswordChange() {
        state {
            copy(
                newPasswordErrorRes = validator.checkError(newPasswordText, ValidationType.Password),
                repeatPasswordErrorRes = if (newPasswordText != repeatPasswordText)
                    R.string.passwords_differ else null
            )
        }
        if (listOf(state.newPasswordErrorRes, state.repeatPasswordErrorRes).any { it != null }) {
            return
        }
        commands(ProfileCommand.ChangePassword(
            oldPassword = state.oldPasswordText,
            newPassword = state.newPasswordText
        ))
        state {
            copy(isLoading = true)
        }
    }

    private fun NextBuilder.handleDataChange() {
        // check for errors
        state{
            copy(
                emailErrorTextRes = validator.checkError(state.emailText, ValidationType.Email),
                phoneErrorTextRes = validator.checkError(state.phoneText, ValidationType.Phone)
            )
        }
        if (listOf(state.emailErrorTextRes, state.phoneErrorTextRes).any { it != null }) {
            return
        }
        // all fields are checked
        commands(ProfileCommand.ChangeData(
            name = state.nameText,
            phone = state.phoneText,
            email = state.emailText
        ))
        state {
            copy(isLoading = true)
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.Fail ->{
                news(ProfileNews.GeneralFail, ProfileNews.NavigateBack)
                state { copy(isLoading = false) }
            }
            is CommandEvent.UserLoaded -> {
                with(event.user) {
                    state {
                        copy(
                            user = event.user,
                            nameText = name,
                            emailText = email,
                            phoneText = phone,
                            isLoading = false
                        )
                    }
                }
            }
            CommandEvent.InterestsCleared -> {
                news(ProfileNews.InterestsCleared)
            }
            CommandEvent.DataChangedSuccessfully -> {
                news(ProfileNews.DataChangedSuccessfully)
                state { copy(isLoading = false) }
            }
            CommandEvent.ChangeFailed -> {
                news(ProfileNews.GeneralFail)
                state { copy(isLoading = false) }
            }
            CommandEvent.PasswordChangedSuccessfully -> {
                news(ProfileNews.PasswordChangedSuccessfully)
                state { copy(isLoading = false) }
            }
        }
    }
}
