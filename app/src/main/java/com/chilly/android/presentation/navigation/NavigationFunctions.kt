package com.chilly.android.presentation.navigation

import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.End
import androidx.compose.animation.AnimatedContentTransitionScope.SlideDirection.Companion.Start
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable

//slides in and out in the same direction
inline fun <reified T : Any> NavGraphBuilder.slidingComposable(
    direction: AnimatedContentTransitionScope.SlideDirection = Start,
    crossinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            slideIntoContainer(
                towards = direction,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = direction,
                animationSpec = tween(500)
            )
        }
    ) {
        content(it)
    }
}

// slide in and back out towards origin
inline fun <reified T : Any> NavGraphBuilder.peekingComposable(
    crossinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            slideIntoContainer(
                towards = Start,
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            slideOutOfContainer(
                towards = End,
                animationSpec = tween(500)
            )
        }
    ) {
        content(it)
    }
}

// fade
inline fun <reified T : Any> NavGraphBuilder.fadingComposable(
    crossinline content: @Composable AnimatedContentScope.(NavBackStackEntry) -> Unit
) {
    composable<T>(
        enterTransition = {
            fadeIn(
                animationSpec = tween(500)
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(500)
            )
        }
    ) {
        content(it)
    }
}