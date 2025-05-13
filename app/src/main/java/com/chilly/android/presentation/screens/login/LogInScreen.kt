package com.chilly.android.presentation.screens.login

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
import com.chilly.android.activityComponent
import com.chilly.android.di.screens.DaggerLoginComponent
import com.chilly.android.di.screens.LoginComponent
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
import com.chilly.android.presentation.screens.login.LoginEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun LogInScreen(
    state: LoginState,
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit = {},
) {
    FormSurface(scaffoldPadding) {
        Text(
            text = stringResource(R.string.login_title),
            style = MaterialTheme.typography.headlineLarge,
            textAlign = TextAlign.Center,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        CancellableTextField(
            text = state.loginText,
            onValueChange = { onEvent(UiEvent.LoginChanged(it)) },
            onClear = { onEvent(UiEvent.ClearClicked) },
            labelTextRes = R.string.username_label,
            placeholderTextRes = R.string.username_placeholder
        )
        Spacer(modifier = Modifier.height(4.dp))
        PasswordTextField(
            text = state.passwordText,
            passwordShown = state.passwordShown,
            onValueChange = { onEvent(UiEvent.PasswordChanged(it)) },
            labelTextRes = R.string.password_label,
            placeholderTextRes = R.string.password_placeholder,
            onVisibilityToggle = { onEvent(UiEvent.ShowPasswordToggled) }
        )
        ChillyButton(
            textRes = R.string.forgot_password_button,
            type = ChillyButtonType.Tertiary,
            onClick = { onEvent(UiEvent.ForgotPasswordClicked) },
            modifier = Modifier.align(Alignment.End)
        )
        Spacer(modifier = Modifier.height(36.dp))
        ChillyButton(
            textRes = R.string.login_button,
            size = SizeParameter.Medium,
            enabled = state.loginButtonEnabled,
            modifier = Modifier.fillMaxWidth(),
            onClick = { onEvent(UiEvent.LogInClicked) }
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

fun NavGraphBuilder.installLoginComposable(padding: PaddingValues) {
    slidingComposable<Destination.LogIn> {
        ScreenHolder(
            componentFactory = {
                DaggerLoginComponent.builder()
                    .appComponent(activityComponent)
                    .build()
            },
            storeFactory = LoginComponent::store
        ) {
            val state = collectState()
            LogInScreen(state.value, padding, store::dispatch)
            NewsCollector(component.newsCollector)
        }
    }
}


@Composable
@PreviewLightDark
@Preview(name = "login screen", showSystemUi = true, showBackground = true)
private fun PreviewLoginScreen() {
    ChillyTheme {
        LogInScreen(
            LoginState(
                loginText = "login",
                passwordText = "password"
            ),
            PaddingValues()
        )
    }
}

