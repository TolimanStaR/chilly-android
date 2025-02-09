package com.chilly.android.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.screens.forgot_password.installForgotPasswordScreen
import com.chilly.android.presentation.screens.login.installLoginComposable
import com.chilly.android.presentation.screens.main.mainScreenComposable
import com.chilly.android.presentation.screens.onboarding.installOnboardingComposable
import com.chilly.android.presentation.screens.sign_up.installSignUpComposable
import com.chilly.android.presentation.screens.splash.installSplashComposable
import kotlin.reflect.KClass

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

    ChillyScaffold(
        navController,
        component.snackbarHostState,
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Destination.Splash,
            enterTransition = { EnterTransition.None },
            exitTransition = { ExitTransition.None }
        ) {
            installSplashComposable()
            installOnboardingComposable(innerPadding)
            mainScreenComposable(navController)
            installLoginComposable(innerPadding)
            installSignUpComposable(innerPadding)
            installForgotPasswordScreen(innerPadding)
        }
    }
}

@Composable
fun ChillyScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    Scaffold(
        topBar = {
            when {
                else -> Unit
            }
        },
        bottomBar = {
            when {
                backStackEntry.matches(Destination.Main) -> Unit
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        content(it)
    }
}

private inline fun <reified T : Destination> NavBackStackEntry?.matches(route: T): Boolean =
    this?.destination?.hasRoute<T>() ?: false