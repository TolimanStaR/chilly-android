package com.chilly.android.presentation.screens.forgotPassword

import com.chilly.android.presentation.common.logic.FieldValidator
import com.chilly.android.presentation.common.logic.ValidationType
import com.chilly.android.utils.testUpdate
import io.mockk.clearMocks
import io.mockk.coEvery
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
            initialState = ForgotPasswordState(
                emailText = "email@mail.com",
                step = RecoveryStep.EMAIL,
                sendButtonEnabled = true
            ),
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
            initialState = ForgotPasswordState(
                emailText = "email@mail.com",
                codeText = "111111",
                step = RecoveryStep.CODE,
                sendButtonEnabled = true
            ),
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
            initialState = ForgotPasswordState(
                emailText = "email@mail.com",
                codeText = "111111",
                passwordText = "11111111",
                passwordRepeatText = "11111111",
                step = RecoveryStep.NEW_PASSWORD,
                sendButtonEnabled = true
            ),
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = {
                listOf(
                    ForgotPasswordCommand.ChangePassword(
                        state.emailText,
                        state.codeText,
                        state.passwordText
                    )
                )
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
            initialState = ForgotPasswordState(
                emailText = "email@mail.com",
                step = RecoveryStep.EMAIL,
                sendButtonEnabled = true,
                isLoading = true
            ),
            event = ForgotPasswordEvent.CommandEvent.CodeSent,
            expectedStateProducer = {
                state.copy(
                    isLoading = false,
                    sendButtonEnabled = false,
                    step = RecoveryStep.CODE
                )
            },
        )
    }

    @Test
    fun `when email text changed, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(emailText = "old@email.com"),
            event = ForgotPasswordEvent.UiEvent.EmailTextChanged("new@email.com"),
            expectedStateProducer = {
                state.copy(
                    emailText = "new@email.com",
                    emailErrorResId = null,
                    sendButtonEnabled = true
                )
            }
        )
    }

    @Test
    fun `when email text cleared, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                emailText = "test@email.com",
                sendButtonEnabled = true
            ),
            event = ForgotPasswordEvent.UiEvent.EmailTextCleared,
            expectedStateProducer = {
                state.copy(emailText = "", sendButtonEnabled = false)
            }
        )
    }

    @Test
    fun `when sign up clicked, navigation news is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(),
            event = ForgotPasswordEvent.UiEvent.SignUpClicked,
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.NavigateSignUp)
            }
        )
    }

    @Test
    fun `when back pressed in email step, navigate to login`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(step = RecoveryStep.EMAIL),
            event = ForgotPasswordEvent.UiEvent.BackPressed,
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.NavigateLogin)
            }
        )
    }

    @Test
    fun `when main button clicked with valid email, command is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                emailText = "valid@email.com",
                step = RecoveryStep.EMAIL,
                sendButtonEnabled = true
            ),
            configuration = {
                every { validatorMock.checkError(any(), any()) } returns null
            },
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = {
                state.copy(isLoading = true)
            },
            expectedCommandsProducer = {
                listOf(ForgotPasswordCommand.SendCodeFor("valid@email.com"))
            }
        )
    }

    @Test
    fun `when code text cleared, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.CODE,
                codeText = "123456",
                sendButtonEnabled = true
            ),
            event = ForgotPasswordEvent.UiEvent.CodeTextCleared,
            expectedStateProducer = {
                state.copy(
                    codeText = "",
                    sendButtonEnabled = false
                )
            }
        )
    }

    @Test
    fun `when back pressed in code step, return to email step`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(step = RecoveryStep.CODE),
            event = ForgotPasswordEvent.UiEvent.BackPressed,
            expectedStateProducer = {
                state.copy(step = RecoveryStep.EMAIL)
            }
        )
    }

    @Test
    fun `when main button clicked with valid code, command is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                emailText = "test@email.com",
                step = RecoveryStep.CODE,
                codeText = "123456",
                sendButtonEnabled = true
            ),
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = {
                state.copy(isLoading = true)
            },
            expectedCommandsProducer = {
                listOf(ForgotPasswordCommand.VerifyCode("test@email.com", "123456"))
            }
        )
    }

    // UI Event Tests - New Password Step

    @Test
    fun `when password text changed, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordText = "oldpass"
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordTextChanged("newpass"),
            expectedStateProducer = {
                state.copy(
                    passwordText = "newpass",
                    passwordErrorResId = null
                )
            }
        )
    }

    @Test
    fun `when password visibility toggled, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordShown = false
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordVisibilityToggled,
            expectedStateProducer = {
                state.copy(passwordShown = true)
            }
        )
    }

    @Test
    fun `when repeat password changed, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordRepeatText = "oldpass"
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordRepeatChanged("newpass"),
            expectedStateProducer = {
                state.copy(
                    passwordRepeatText = "newpass",
                    repeatPasswordErrorResId = null
                )
            }
        )
    }

    @Test
    fun `when repeat password visibility toggled, state is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordRepeatShown = false
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordRepeatVisibilityToggled,
            expectedStateProducer = {
                state.copy(passwordRepeatShown = true)
            }
        )
    }

    @Test
    fun `when back pressed in new password step, return to code step`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(step = RecoveryStep.NEW_PASSWORD),
            event = ForgotPasswordEvent.UiEvent.BackPressed,
            expectedStateProducer = {
                state.copy(step = RecoveryStep.CODE)
            }
        )
    }

    @Test
    fun `when main button clicked with matching passwords, command is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                emailText = "test@email.com",
                codeText = "123456",
                step = RecoveryStep.NEW_PASSWORD,
                passwordText = "newpassword",
                passwordRepeatText = "newpassword",
                sendButtonEnabled = true
            ),
            configuration = {
                every { validatorMock.checkError(any(), any()) } returns null
            },
            event = ForgotPasswordEvent.UiEvent.MainButtonClicked,
            expectedStateProducer = {
                state.copy(isLoading = true)
            },
            expectedCommandsProducer = {
                listOf(
                    ForgotPasswordCommand.ChangePassword(
                        "test@email.com",
                        "123456",
                        "newpassword"
                    )
                )
            }
        )
    }

    // Command Event Tests

    @Test
    fun `when code sent, step is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.EMAIL,
                isLoading = true
            ),
            event = ForgotPasswordEvent.CommandEvent.CodeSent,
            expectedStateProducer = {
                state.copy(
                    isLoading = false,
                    step = RecoveryStep.CODE,
                    sendButtonEnabled = false
                )
            }
        )
    }

    @Test
    fun `when email rejected, news is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(isLoading = true),
            event = ForgotPasswordEvent.CommandEvent.EmailRejected,
            expectedStateProducer = {
                state.copy(
                    isLoading = false,
                    sendButtonEnabled = false
                )
            },
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.WrongEmail)
            }
        )
    }

    @Test
    fun `when command fails, news is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(isLoading = true),
            event = ForgotPasswordEvent.CommandEvent.CommandFail,
            expectedStateProducer = {
                state.copy(isLoading = false)
            },
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.FailedRequest)
            }
        )
    }

    @Test
    fun `when password changed, news is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(isLoading = true),
            event = ForgotPasswordEvent.CommandEvent.PasswordChanged,
            expectedStateProducer = {
                state.copy(isLoading = false)
            },
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.PasswordChanged, ForgotPasswordNews.NavigateLogin)
            }
        )
    }

    @Test
    fun `when verified successfully, step is updated`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.CODE,
                isLoading = true
            ),
            event = ForgotPasswordEvent.CommandEvent.VerifiedSuccessfully,
            expectedStateProducer = {
                state.copy(
                    isLoading = false,
                    step = RecoveryStep.NEW_PASSWORD,
                    sendButtonEnabled = false
                )
            }
        )
    }

    @Test
    fun `when wrong code received, news is sent`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(isLoading = true),
            event = ForgotPasswordEvent.CommandEvent.WrongCode,
            expectedStateProducer = {
                state.copy(isLoading = false)
            },
            expectedNewsProducer = {
                listOf(ForgotPasswordNews.WrongCode)
            }
        )
    }

    // Complex scenarios

    @Test
    fun `when both password fields are filled, button is enabled`() {
        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordText = "",
                passwordRepeatText = ""
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordTextChanged("password"),
            expectedStateProducer = {
                state.copy(
                    passwordText = "password",
                    sendButtonEnabled = false
                )
            }
        )

        underTest.testUpdate(
            initialState = ForgotPasswordState(
                step = RecoveryStep.NEW_PASSWORD,
                passwordText = "password",
                passwordRepeatText = ""
            ),
            event = ForgotPasswordEvent.UiEvent.PasswordRepeatChanged("password"),
            expectedStateProducer = {
                state.copy(
                    passwordRepeatText = "password",
                    sendButtonEnabled = true
                )
            }
        )
    }
}
