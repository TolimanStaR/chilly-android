package com.chilly.android.presentation.screens.splash

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavGraphBuilder
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerSplashScreenComponent
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.fadingComposable
import com.chilly.android.presentation.screens.result.NotificationWork
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun SplashScreen(
    onEvent: (SplashScreenEvent) -> Unit
) {
    val activity = LocalContext.current as? Activity
    LaunchedEffect(Unit) {
        activity?.intent?.extras?.let { bundle ->
            val ids = bundle.getIntArray(NotificationWork.INPUT_PLACE_IDS)
            if (ids != null) {
                onEvent(SplashScreenEvent.GotNotificationEvent(ids.toList()))
                return@LaunchedEffect
            }
        }
        onEvent(SplashScreenEvent.GotRegularIntent)
    }

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
    fadingComposable<Destination.Splash> {
        ScreenHolder(
            componentFactory = {
                DaggerSplashScreenComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            viewModelFactory = { viewModel() }
        ) {
            SplashScreen(viewModel::dispatch)
        }
    }
}


@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
private fun PreviewSplashScreen() {
    ChillyTheme {
        SplashScreen {}
    }
}