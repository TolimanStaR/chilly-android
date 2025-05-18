package com.chilly.android.presentation.screens.login

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.presentation.screens.WithTheme

@Composable
@PreviewLightDark
@Preview(showBackground = true)
fun PreviewLoginScreen() = WithTheme {
    LogInScreen(
        LoginState(
            loginText = "login",
            passwordText = "password"
        ),
        PaddingValues()
    )
}