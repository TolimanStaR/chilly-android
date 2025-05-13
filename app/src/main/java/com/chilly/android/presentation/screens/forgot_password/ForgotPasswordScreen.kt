package com.chilly.android.presentation.screens.forgot_password

import androidx.activity.compose.BackHandler
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerForgotPasswordComponent
import com.chilly.android.presentation.common.components.CancellableTextField
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.FormSurface
import com.chilly.android.presentation.common.components.PasswordTextField
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.slidingComposable
import com.chilly.android.presentation.screens.forgot_password.ForgotPasswordEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun ForgotPasswordScreen(
    state: ForgotPasswordState,
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    BackHandler { onEvent(UiEvent.BackPressed) }
    FormSurface(scaffoldPadding) {
        Row(
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = { onEvent(UiEvent.BackPressed) },
                colors = IconButtonDefaults.iconButtonColors(
                    contentColor = MaterialTheme.colorScheme.primary
                )
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = null
                )
            }
            Text(
                text = stringResource(R.string.password_recovery_title),
                style = MaterialTheme.typography.headlineLarge,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = stringResource(R.string.recovery_step_title, state.step.ordinal + 1, RecoveryStep.entries.size),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.secondary,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            // todo slide in and out
            when (state.step) {
                RecoveryStep.EMAIL -> EmailStepFields(state, onEvent)
                RecoveryStep.CODE -> CodeVerificationStepFields(state, onEvent)
                RecoveryStep.NEW_PASSWORD -> PasswordChangeStepFields(state, onEvent)
            }
        }
        Spacer(modifier = Modifier.height(36.dp))
        ChillyButton(
            textRes = state.step.toMainButtonTextRes(),
            size = SizeParameter.Medium,
            enabled = state.sendButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(UiEvent.MainButtonClicked) }
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChillyButton(
            textRes = R.string.sign_up_button,
            size = SizeParameter.Medium,
            type = ChillyButtonType.Secondary,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(UiEvent.SignUpClicked) }
        )
        if (state.isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun EmailStepFields(state: ForgotPasswordState, onEvent: (UiEvent) -> Unit) {
    CancellableTextField(
        text = state.emailText,
        onValueChange = { onEvent(UiEvent.EmailTextChanged(it)) },
        onClear = { onEvent(UiEvent.EmailTextCleared) },
        placeholderTextRes = R.string.username_placeholder,
        errorText = state.emailErrorResId?.let { stringResource(it) }
    )
}

@Composable
private fun CodeVerificationStepFields(state: ForgotPasswordState, onEvent: (UiEvent) -> Unit) {
    Text(
        text = stringResource(R.string.used_email_template, state.emailText),
        style = MaterialTheme.typography.bodyLarge,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.fillMaxWidth()
    )
    CancellableTextField(
        text = state.codeText,
        onValueChange = { onEvent(UiEvent.CodeTextChanged(it)) },
        onClear = { onEvent(UiEvent.CodeTextCleared) },
        placeholderTextRes = R.string.code_placeholder
    )
}

@Composable
private fun PasswordChangeStepFields(state: ForgotPasswordState, onEvent: (UiEvent) -> Unit) {
    PasswordTextField(
        text = state.passwordText,
        passwordShown = state.passwordShown,
        onValueChange = { onEvent(UiEvent.PasswordTextChanged(it)) },
        labelTextRes = R.string.sign_up_password_label,
        placeholderTextRes = R.string.sign_up_password_label,
        onVisibilityToggle = { onEvent(UiEvent.PasswordVisibilityToggled) },
        errorText = state.passwordErrorResId?.let { stringResource(it) }
    )
    PasswordTextField(
        text = state.passwordRepeatText,
        passwordShown = state.passwordRepeatShown,
        onValueChange = { onEvent(UiEvent.PasswordRepeatChanged(it)) },
        labelTextRes = R.string.sign_up_password_repeat_label,
        placeholderTextRes = R.string.sign_up_password_repeat_label,
        onVisibilityToggle = { onEvent(UiEvent.PasswordRepeatVisibilityToggled) },
        errorText = state.repeatPasswordErrorResId?.let { stringResource(it) }
    )
}

@StringRes
private fun RecoveryStep.toMainButtonTextRes() = when(this) {
    RecoveryStep.EMAIL -> R.string.main_button_1_text
    RecoveryStep.CODE -> R.string.main_button_2_text
    RecoveryStep.NEW_PASSWORD -> R.string.main_button_3_text
}

fun NavGraphBuilder.installForgotPasswordScreen(padding: PaddingValues) {
    slidingComposable<Destination.ForgotPassword> {
        ScreenHolder(
            componentFactory = {
                DaggerForgotPasswordComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            ForgotPasswordScreen(state.value, padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "login screen", showSystemUi = true, showBackground = true)
private fun PreviewLoginScreen(
    @PreviewParameter(StepProvider::class) step: RecoveryStep
) {
    ChillyTheme {
        ForgotPasswordScreen(
            state = ForgotPasswordState(
                step = step
            ),
            PaddingValues()
        ) { }
    }
}

private class StepProvider : PreviewParameterProvider<RecoveryStep> {
    override val values = RecoveryStep.entries.asSequence()
}