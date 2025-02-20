package com.chilly.android.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.LifecycleResumeEffect
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.screens.forgot_password.installForgotPasswordScreen
import com.chilly.android.presentation.screens.login.installLoginComposable
import com.chilly.android.presentation.screens.main.installMainScreen
import com.chilly.android.presentation.screens.onboarding.installOnboardingComposable
import com.chilly.android.presentation.screens.profile.installProfileScreen
import com.chilly.android.presentation.screens.quiz.installQuizScreen
import com.chilly.android.presentation.screens.sign_up.installSignUpComposable
import com.chilly.android.presentation.screens.splash.installSplashComposable
import com.github.terrakok.cicerone.Router

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
            installMainScreen(innerPadding)
            installLoginComposable(innerPadding)
            installSignUpComposable(innerPadding)
            installForgotPasswordScreen(innerPadding)
            installProfileScreen(innerPadding)
            installQuizScreen(innerPadding)

            // TODO() replace when implemented
            installStubScreen<Destination.Favorites>("favorites", innerPadding)
            installStubScreen<Destination.History>("history", innerPadding)
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

    val component = LocalContext.current.applicationComponent

    Scaffold(
        topBar = {
            val state = backStackEntry.topBarState() ?: return@Scaffold
            val handler = remember { TopBarNavigationHandler(component.router) }
            ChillyTopBar(state, handler::onEvent)
        },
        bottomBar = {
            if (backStackEntry.isInBottomDestinations()) {
                ChillyBottomBar(backStackEntry) {
                    component.router.backTo(it)
                }
            }
        },
        snackbarHost = {
            SnackbarHost(snackbarHostState)
        }
    ) {
        content(it)
    }
}

private class TopBarNavigationHandler(
    private val router: Router
) {
    fun onEvent(event: TopBarEvent) {
        when(event) {
            TopBarEvent.BackClicked -> router.exit()
            TopBarEvent.ProfileClicked -> router.navigateTo(Destination.Profile)
        }
    }
}


private inline fun <reified T : Destination> NavBackStackEntry?.matches(): Boolean =
    this?.destination?.hasRoute<T>() ?: false

private fun NavBackStackEntry?.matchesAny(vararg routes: Destination): Boolean {
    this ?: return false
    return routes.any { destination.hasRoute(it::class) }
}

private fun NavBackStackEntry?.topBarState(): TopBarState? {
    return when {
        matches<Destination.Main>() -> TopBarState(R.string.main_screen_title)
        matches<Destination.Profile>() -> TopBarState(R.string.profile_screen_title, showBackButton = true, showProfileAction = false)
        matches<Destination.History>() -> TopBarState(R.string.history_screen_title)
        matches<Destination.Favorites>() -> TopBarState(R.string.favorites_screen_title)
        matches<Destination.Quiz>() -> TopBarState(R.string.quiz_screen_title, showBackButton = true, showProfileAction = false)
        else -> null
    }
}

private fun NavBackStackEntry?.isInBottomDestinations(): Boolean {
    return matchesAny(*bottomNavigationRoutes.map { it.route }.toTypedArray())
}

// TODO() remove when screens are implemented
private inline fun <reified D : Destination> NavGraphBuilder.installStubScreen(name: String, padding: PaddingValues) {
    composable<D> {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(name)
        }
    }
}

// TODO() remove when screens are implemented
private inline fun <reified D : Destination> NavGraphBuilder.installStubScreen(padding: PaddingValues, crossinline name: (D) -> String) {
    composable<D> { backStackEntry ->
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            Text(name.invoke(backStackEntry.toRoute()))
        }
    }
}