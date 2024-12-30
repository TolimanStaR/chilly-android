package com.chilly.android.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import kotlinx.serialization.Serializable

private val RED_COLOR = Color(0xFFFB3B42)

@Composable
fun SplashScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = RED_COLOR)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.chilly_icon),
            contentDescription = null
        )
    }
}

fun NavGraphBuilder.splashComposable() {
    composable<Splash> {
        SplashScreen()
    }
}

@Serializable
data object Splash

@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
fun PreviewSplashScreen() {
    SplashScreen()
}