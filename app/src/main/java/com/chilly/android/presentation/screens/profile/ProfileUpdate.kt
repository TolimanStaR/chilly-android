package com.chilly.android.presentation.screens.profile

import com.chilly.android.presentation.screens.profile.ProfileEvent.CommandEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class ProfileUpdate @Inject constructor(

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
                state { copy(emailText = event.newValue) }
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
                state { copy(phoneText = event.newValue)}
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
                .all { (text, userValue) -> text.isNotBlank() && text != userValue }
        }
        state { copy(changeDataEnabled = enabled) }
    }

    private fun handlePasswordChange() {

    }

    private fun handleDataChange() {

    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.Fail -> news(ProfileNews.GeneralFail, ProfileNews.NavigateBack)
            is CommandEvent.UserLoaded -> {
                with(event.user) {
                    state {
                        copy(
                            user = event.user,
                            nameText = name,
                            emailText = email,
                            phoneText = phone
                        )
                    }
                }
            }
            CommandEvent.InterestsCleared -> {
                news(ProfileNews.InterestsCleared)
            }
        }
    }
}
