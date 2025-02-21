package com.chilly.android.presentation.common.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.chilly.android.R
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
fun LoadingPlaceholder(
    text: String? = null,
    startLoading: () -> Unit = {},
) {
    LaunchedEffect(Unit) { startLoading() }
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            CircularProgressIndicator()
            text?.let {
                Text(
                    text = it
                )
            }
        }
    }
}

@Composable
fun ErrorReloadPlaceHolder(
    text: String? = null,
    onClick: () -> Unit = {},
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            text?.let {
                Text(
                    text = it
                )
            }
            ChillyButton(
                textRes = R.string.try_again_button,
                onClick = onClick
            )
        }
    }
}

@Preview
@Composable
fun PreviewLoadingScreen() {
    ChillyTheme {
        LoadingPlaceholder("hello hello hello hello")
    }
}

@Preview
@Composable
fun PreviewErrorScreen() {
    ChillyTheme {
        ErrorReloadPlaceHolder("hello hello hello hello")
    }
}