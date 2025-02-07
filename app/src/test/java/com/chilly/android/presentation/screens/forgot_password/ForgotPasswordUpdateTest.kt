package com.chilly.android.presentation.screens.forgot_password

import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.presentation.common.logic.ValidationType
import com.chilly.android.utils.testUpdate
import io.mockk.clearMocks
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Test

class ForgotPasswordUpdateTest {

    private val validatorMock: FieldValidator = mockk()

    private val underTest = ForgotPasswordUpdate(validatorMock)

    @AfterEach
    fun tearDown() {
        clearMocks(validatorMock)
    }

    @Test
    fun `when email entered value is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "before", sendButtonEnabled = true),
            event = ForgotPasswordEvent.UiEvent.EmailTextChanged("after"),
            expectedStateProducer = {
                state.copy(emailText = event.newValue)
            }
        )
    }

    @Test
    fun `when email cleared text is empty`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "before"),
            event = ForgotPasswordEvent.UiEvent.EmailTextCleared,
            expectedStateProducer = {
                state.copy(emailText = "")
            }
        )
    }

    @Test
    fun `when main button clicked on 1 step send code command produced`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "email@mail.com", step = RecoveryStep.EMAIL, sendButtonEnabled = true),
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = {
                listOf(ForgotPasswordCommand.SendCodeFor(state.emailText))
            },
            configuration = {
                every { validatorMock.checkError(any(), any()) } returns null
            },
            verification = {
                verify { validatorMock.checkError(any(), eq(ValidationType.Email)) }
            }
        )
    }

    @Test
    fun `when main button clicked on 2 step verification command is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "email@mail.com", codeText = "111111", step = RecoveryStep.CODE, sendButtonEnabled = true),
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = {
                listOf(ForgotPasswordCommand.VerifyCode(state.emailText, code = state.codeText))
            }
        )
    }

    @Test
    fun `when main button clicked on 3 step send code command produced`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "email@mail.com", codeText = "111111", passwordText = "11111111", passwordRepeatText = "11111111", step = RecoveryStep.NEW_PASSWORD, sendButtonEnabled = true),
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = {
                listOf(ForgotPasswordCommand.ChangePassword(state.emailText, state.codeText, state.passwordText))
            },
            configuration = {
                every { validatorMock.checkError(any(), any()) } returns null
            },
            verification = {
                verify { validatorMock.checkError(any(), eq(ValidationType.Password)) }
            }
        )
    }

    @Test
    fun `when code sent successfully, navigate to code verification step`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "email@mail.com", step = RecoveryStep.EMAIL, sendButtonEnabled = true, isLoading = true),
            event = ForgotPasswordEvent.CommandEvent.CodeSent,
            expectedStateProducer = { state.copy(isLoading = false, sendButtonEnabled = false, step = RecoveryStep.CODE) },
        )
    }
}