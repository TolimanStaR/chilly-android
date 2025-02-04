package com.chilly.android.presentation.sign_up

import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.utils.testUpdate
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class SignUpUpdateTest {

    private val validatorMock: FieldValidator = mockk()

    private val underTest = SignUpUpdate(validatorMock)

    @AfterEach
    fun tearDown() {
        clearMocks(validatorMock)
    }

    @Test
    fun `when name text updated new value stored`() {
        underTest.testUpdate(
            initialState = SignUpState(nameText = "before"),
            event = SignUpEvent.UiEvent.NameTextChanged("after"),
            expectedStateProducer = {
                state.copy(nameText = "after")
            }
        )
    }

    @Test
    fun `when name text cleared value empties`() {
        underTest.testUpdate(
            initialState = SignUpState(nameText = "before", signUpEnabled = true),
            event = SignUpEvent.UiEvent.ClearNameClicked,
            expectedStateProducer = {
                state.copy(nameText = "", signUpEnabled = false)
            }
        )
    }

    @Test
    fun `when sign up clicked verification executed`() {
        underTest.testUpdate(
            initialState = SignUpState(
                emailText = "email",
                passwordText = "password",
                passwordRepeatText = "password",
                signUpEnabled = true
            ),
            event = SignUpEvent.UiEvent.SignUpClicked,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = {
                with(state) {
                    listOf(SignUpCommand.SignUp(nameText, emailText, phoneText, passwordText))
                }
            },
            configuration = {
                every { validatorMock.checkError(any(), any()) } returns null
            },
            verification = {
                verify { validatorMock.checkError(any(), any()) }
            }
        )
    }
}