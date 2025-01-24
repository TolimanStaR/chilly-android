package com.chilly.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.login.installLoginComposable
import com.chilly.android.presentation.main.mainScreenComposable
import com.chilly.android.presentation.onboarding.onboardingComposable
import com.chilly.android.presentation.splash.splashComposable

@Composable
fun ChillyNavHost(navController: NavHostController = rememberNavController()) {
    val navigator = remember {
        ComposeNavigator(navController)
    }

    val component = LocalContext.current.applicationComponent

    LifecycleResumeEffect(navigator) {
        component.navigatorHolder.setNavigator(navigator)

        onPauseOrDispose {
            component.navigatorHolder.removeNavigator()
        }
    }

    NavHost(navController, Destination.Splash) {
        splashComposable()
        onboardingComposable()
        mainScreenComposable(navController)
        installLoginComposable()
    }
}