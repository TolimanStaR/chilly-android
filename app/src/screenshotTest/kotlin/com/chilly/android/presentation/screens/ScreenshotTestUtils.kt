package com.chilly.android.presentation.screens

import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
fun WithTheme(
    content: @Composable () -> Unit
) {
    ChillyTheme {
        Surface {
            content()
        }
    }
}