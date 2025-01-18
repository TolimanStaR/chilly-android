package com.chilly.android.presentation.splash

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerSplashScreenComponent
import com.chilly.android.di.screens.SplashScreenComponent
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.clearStackAndNavigate
import com.chilly.android.presentation.theme.Red50

@Composable
private fun SplashScreen(
    backgroundWork: () -> Unit
) {
    LaunchedEffect(true) {
        backgroundWork.invoke()
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = Red50)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.chilly_icon),
            contentDescription = null
        )
    }
}

fun NavGraphBuilder.splashComposable(navController: NavController) {
    composable<Destination.Splash> {
        ScreenHolder(
            viewModelFactory = {
                buildComponent().viewModelFactory().build(navController::clearStackAndNavigate)
            }
        ) {
            SplashScreen(::tryLogin)
        }
    }
}

private fun Context.buildComponent(): SplashScreenComponent = DaggerSplashScreenComponent.builder()
    .appComponent(applicationComponent)
    .build()

@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
private fun PreviewSplashScreen() {
    SplashScreen {}
}