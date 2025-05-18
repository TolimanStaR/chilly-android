package com.chilly.android.presentation.screens.signUp

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.R
import com.chilly.android.presentation.screens.WithTheme

@Composable
@PreviewLightDark
@Preview(showBackground = true)
fun PreviewSignUpScreen() = WithTheme {
    SignUpScreen(
        SignUpState(
            emailErrorRedId = R.string.app_name
        ),
        PaddingValues()
    )
}