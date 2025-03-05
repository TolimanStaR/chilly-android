package com.chilly.android.presentation.screens.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerSplashScreenComponent
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun SplashScreen() {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .background(color = MaterialTheme.colorScheme.primary)
            .fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.chilly_icon),
            contentDescription = null
        )
    }
}

fun NavGraphBuilder.installSplashComposable() {
    composable<Destination.Splash> {
        ScreenHolder(
            componentFactory = {
                DaggerSplashScreenComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            viewModelFactory = { viewModel() }
        ) {
            SplashScreen()
        }
    }
}


@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
private fun PreviewSplashScreen() {
    ChillyTheme {
        SplashScreen()
    }
}