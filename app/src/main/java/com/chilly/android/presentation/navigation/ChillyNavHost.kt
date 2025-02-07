package com.chilly.android.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.screens.forgot_password.installForgotPasswordScreen
import com.chilly.android.presentation.screens.login.installLoginComposable
import com.chilly.android.presentation.screens.main.mainScreenComposable
import com.chilly.android.presentation.screens.onboarding.installOnboardingComposable
import com.chilly.android.presentation.screens.sign_up.installSignUpComposable
import com.chilly.android.presentation.screens.splash.installSplashComposable

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

    Surface {
        NavHost(
            navController = navController,
            startDestination = Destination.Splash,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            installSplashComposable()
            installOnboardingComposable()
            mainScreenComposable(navController)
            installLoginComposable()
            installSignUpComposable()
            installForgotPasswordScreen()
        }
    }
}