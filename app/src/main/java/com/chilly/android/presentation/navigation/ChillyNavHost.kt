package com.chilly.android.presentation.navigation

import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
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
import com.chilly.android.R
import com.chilly.android.activityComponent
import com.chilly.android.presentation.screens.favorites.installFavoritesScreen
import com.chilly.android.presentation.screens.forgot_password.installForgotPasswordScreen
import com.chilly.android.presentation.screens.history.HistoryActions
import com.chilly.android.presentation.screens.history.installHistoryScreen
import com.chilly.android.presentation.screens.login.installLoginComposable
import com.chilly.android.presentation.screens.main.installMainScreen
import com.chilly.android.presentation.screens.onboarding.installOnboardingComposable
import com.chilly.android.presentation.screens.place.installPlaceInfoScreen
import com.chilly.android.presentation.screens.profile.installProfileScreen
import com.chilly.android.presentation.screens.quiz.installQuizScreen
import com.chilly.android.presentation.screens.rating.installRatingScreen
import com.chilly.android.presentation.screens.result.installRecommendationResultScreen
import com.chilly.android.presentation.screens.sign_up.installSignUpComposable
import com.chilly.android.presentation.screens.splash.installSplashComposable

@OptIn(ExperimentalSharedTransitionApi::class)
@Composable
fun ChillyNavHost(navController: NavHostController = rememberNavController()) {
    val navigator = remember {
        ComposeNavigator(navController)
    }

    val component = LocalContext.current.activityComponent

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
        SharedTransitionScope { sharedTransitionModifier ->
            NavHost(
                navController = navController,
                startDestination = Destination.Splash,
                enterTransition = { EnterTransition.None },
                exitTransition = { ExitTransition.None },
                modifier = sharedTransitionModifier
            ) {
                installSplashComposable()
                installOnboardingComposable(innerPadding)
                installMainScreen(innerPadding, this@SharedTransitionScope)
                installLoginComposable(innerPadding)
                installSignUpComposable(innerPadding)
                installForgotPasswordScreen(innerPadding)
                installProfileScreen(innerPadding)
                installQuizScreen(innerPadding)
                installRecommendationResultScreen(innerPadding)
                installPlaceInfoScreen(innerPadding, this@SharedTransitionScope)
                installHistoryScreen(innerPadding)
                installFavoritesScreen(innerPadding)
                installRatingScreen(innerPadding)
            }
        }
    }
}

private fun NavBackStackEntry?.topBarState(): TopBarState? {
    return when {
        matches<Destination.Main>() -> TopBarState(R.string.main_screen_title, actions = listOf(TopBarAction.Profile))
        matches<Destination.Profile>() -> TopBarState(R.string.profile_screen_title, showBackButton = true)
        matches<Destination.Favorites>() -> TopBarState(R.string.favorites_screen_title, actions = listOf(TopBarAction.Profile))
        matches<Destination.Quiz>() -> TopBarState(R.string.quiz_screen_title, showBackButton = true)
        matches<Destination.RecommendationResult>() -> TopBarState(R.string.recommendation_result_title, showBackButton = true)
        matches<Destination.Rating>() -> TopBarState(R.string.rating_title, showBackButton = true)
        matches<Destination.History>() -> TopBarState(R.string.history_screen_title, actions = listOf(HistoryActions.Clear, TopBarAction.Profile))
        matches<Destination.PlaceInfo>() -> {
            val route = this?.toRoute<Destination.PlaceInfo>() ?: return null
            TopBarState(titleText = route.name, showBackButton = true)
        }
        else -> null
    }
}

@Composable
fun ChillyScaffold(
    navController: NavHostController,
    snackbarHostState: SnackbarHostState,
    content: @Composable (PaddingValues) -> Unit
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val component = LocalContext.current.activityComponent

    Scaffold(
        topBar = {
            val state = backStackEntry.topBarState() ?: return@Scaffold
            ChillyTopBar(state, component.topBarEventHandler::onEvent)
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

private inline fun <reified T : Destination> NavBackStackEntry?.matches(): Boolean =
    this?.destination?.hasRoute<T>() ?: false

private fun NavBackStackEntry?.isInBottomDestinations(): Boolean {
    return matchesAny(*bottomNavigationRoutes.map { it.route }.toTypedArray())
}

private fun NavBackStackEntry?.matchesAny(vararg routes: Destination): Boolean {
    this ?: return false
    return routes.any { destination.hasRoute(it::class) }
}
