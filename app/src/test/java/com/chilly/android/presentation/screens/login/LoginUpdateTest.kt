package com.chilly.android.presentation.screens.login

import com.chilly.android.utils.testUpdate
import org.junit.jupiter.api.Test

class LoginUpdateTest {

    private val underTest = LoginUpdate()

    @Test
    fun `when login text updated new value stored`() {
        underTest.testUpdate(
            initialState = LoginState(loginText = "before"),
            event = LoginEvent.UiEvent.LoginChanged("after"),
            expectedStateProducer = {
                state.copy(loginText = "after")
            }
        )
    }

    @Test
    fun `when password text updated new value stored`() {
        underTest.testUpdate(
            initialState = LoginState(passwordText = "before"),
            event = LoginEvent.UiEvent.PasswordChanged("after"),
            expectedStateProducer = {
                state.copy(passwordText = "after")
            }
        )
    }

    @Test
    fun `when logIn clicked command is send`() {
        underTest.testUpdate(
            initialState = LoginState("a", "b"),
            event = LoginEvent.UiEvent.LogInClicked,
            expectedStateProducer = {
                state.copy(isLoading = true)
            },
            expectedCommandsProducer = {
                listOf(LoginCommand.LogIn(state.loginText, state.passwordText))
            }
        )
    }

    @Test
    fun `when signUp clicked navigation news send`() {
        underTest.testUpdate(
            initialState = LoginState(),
            event = LoginEvent.UiEvent.SignUpClicked,
            expectedNewsProducer = {
                listOf(LoginNews.NavigateSignUp)
            }
        )
    }

    @Test
    fun `when clear clicked navigation login text is cleared`() {
        underTest.testUpdate(
            initialState = LoginState(loginText = "before"),
            event = LoginEvent.UiEvent.ClearClicked,
            expectedStateProducer = {
                state.copy(loginText = "")
            }
        )
    }

    @Test
    fun `when show_password clicked visibility is toggled`() {
        underTest.testUpdate(
            initialState = LoginState(passwordShown = false),
            event = LoginEvent.UiEvent.ShowPasswordToggled,
            expectedStateProducer = {
                state.copy(passwordShown = true)
            }
        )
        underTest.testUpdate(
            initialState = LoginState(passwordShown = true),
            event = LoginEvent.UiEvent.ShowPasswordToggled,
            expectedStateProducer = {
                state.copy(passwordShown = false)
            }
        )
    }

    @Test
    fun `when login fails news is produced`() {
        underTest.testUpdate(
            initialState = LoginState(),
            event = LoginEvent.CommandEvent.LoginFail,
            expectedNewsProducer = {
                listOf(LoginNews.LoginFailed)
            }
        )
    }

    @Test
    fun `when login succeed news is produced`() {
        underTest.testUpdate(
            initialState = LoginState(),
            event = LoginEvent.CommandEvent.LoginSuccess("refresh", "access"),
            expectedNewsProducer = {
                listOf(LoginNews.NavigateMain)
            }
        )
    }
}