package com.chilly.android.presentation.splash

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.common.lazyViewModel
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.navigation.clearStackAndNavigate
import com.chilly.android.presentation.theme.Red50

@Composable
fun SplashScreen(
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

        val context = LocalContext.current
        val component = context.applicationComponent

        val viewModel: SplashScreenViewModel by context.lazyViewModel {
            component.splashScreenViewModelFactory().build(navController::clearStackAndNavigate)
        }

        SplashScreen(viewModel::tryLogin)
    }
}

@Composable
@Preview(name = "splash screen", showSystemUi = true, showBackground = true)
fun PreviewSplashScreen() {
    SplashScreen {}
}