package com.chilly.android.presentation.screens.sign_up

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerSignUpComponent
import com.chilly.android.di.screens.SignUpComponent
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
import com.chilly.android.presentation.screens.sign_up.SignUpEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun SignUpScreen(
    state: SignUpState,
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit = {}
) {
    FormSurface(scaffoldPadding) {
        Text(
            text = stringResource(R.string.sign_up_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            CancellableTextField(
                text = state.nameText,
                onValueChange = { onEvent(UiEvent.NameTextChanged(it)) },
                onClear = { onEvent(UiEvent.ClearNameClicked) },
                labelTextRes = R.string.sign_up_name_label,
                placeholderTextRes = R.string.sign_up_name_label,
            )
            CancellableTextField(
                text = state.emailText,
                onValueChange = { onEvent(UiEvent.EmailTextChanged(it)) },
                onClear = { onEvent(UiEvent.ClearEmailClicked) },
                labelTextRes = R.string.sign_up_email_label,
                placeholderTextRes = R.string.sign_up_email_label,
                errorText = state.emailErrorRedId?.let { stringResource(it) }
            )
            CancellableTextField(
                text = state.phoneText,
                onValueChange = { onEvent(UiEvent.PhoneTextChanged(it)) },
                onClear = { onEvent(UiEvent.ClearPhoneClicked) },
                labelTextRes = R.string.sign_up_phone_label,
                placeholderTextRes = R.string.sign_up_phone_label,
                errorText = state.phoneErrorRedId?.let { stringResource(it) }
            )
            PasswordTextField(
                text = state.passwordText,
                passwordShown = state.passwordShown,
                onValueChange = { onEvent(UiEvent.PasswordTextChanged(it)) },
                labelTextRes = R.string.sign_up_password_label,
                placeholderTextRes = R.string.sign_up_password_label,
                onVisibilityToggle = { onEvent(UiEvent.PasswordVisibilityToggled) },
                errorText = state.passwordErrorRedId?.let { stringResource(it) }
            )
            PasswordTextField(
                text = state.passwordRepeatText,
                passwordShown = state.passwordRepeatShown,
                onValueChange = { onEvent(UiEvent.PasswordRepeatChanged(it)) },
                labelTextRes = R.string.sign_up_password_repeat_label,
                placeholderTextRes = R.string.sign_up_password_repeat_label,
                onVisibilityToggle = { onEvent(UiEvent.PasswordRepeatVisibilityToggled) },
                errorText = state.repeatPasswordErrorRedId?.let { stringResource(it) }
            )
        }
        Spacer(modifier = Modifier.height(36.dp))
        ChillyButton(
            textRes = R.string.sign_up_button,
            size = SizeParameter.Medium,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(UiEvent.SignUpClicked) },
            enabled = state.signUpEnabled
        )
        Spacer(modifier = Modifier.height(12.dp))
        ChillyButton(
            textRes = R.string.sign_up_have_account_button,
            size = SizeParameter.Medium,
            type = ChillyButtonType.Secondary,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(UiEvent.HaveAccountClicked) }
        )
        if (state.isLoading) {
            Spacer(modifier = Modifier.height(12.dp))
            CircularProgressIndicator(
                modifier = Modifier.align(Alignment.CenterHorizontally)
            )
        }
    }
}

fun NavGraphBuilder.installSignUpComposable(padding: PaddingValues) {
    slidingComposable<Destination.SignUp> {
        ScreenHolder(
            componentFactory = {
                DaggerSignUpComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = SignUpComponent::store
        ) {
            val state = collectState()
            SignUpScreen(state.value, padding, store::dispatch)
            NewsCollector(component.newsCollector)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "login screen", showSystemUi = true, showBackground = true)
private fun PreviewLoginScreen() {
    ChillyTheme {
        SignUpScreen(
            SignUpState(
                emailErrorRedId = R.string.app_name
            ),
            PaddingValues()
        )
    }
}