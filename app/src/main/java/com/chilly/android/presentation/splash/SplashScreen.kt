package com.chilly.android.presentation.splash

import android.content.Context
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
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
import com.chilly.android.presentation.common.structure.EffectCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.clearStackAndNavigate
import com.chilly.android.presentation.theme.Red50
import kotlinx.coroutines.flow.FlowCollector

@Composable
private fun SplashScreen() {
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
                buildComponent().viewModelFactory().build()
            }
        ) {
            SplashScreen()
            EffectCollector(createEffectsCollector(navController))
        }
    }
}

private fun Context.buildComponent(): SplashScreenComponent = DaggerSplashScreenComponent.builder()
    .appComponent(applicationComponent)
    .build()

private fun createEffectsCollector(navController: NavController) = FlowCollector<SplashScreenEffect> { effect ->
    when(effect) {
        SplashScreenEffect.NavigateLogin -> navController.clearStackAndNavigate(Destination.LogIn)
        SplashScreenEffect.NavigateMain -> navController.clearStackAndNavigate(Destination.Main)
        SplashScreenEffect.NavigateOnboarding -> navController.clearStackAndNavigate(Destination.Onboarding(0))
    }
}

@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
private fun PreviewSplashScreen() {
    SplashScreen()
}