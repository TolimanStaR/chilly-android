package com.chilly.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hasRoute
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Replace
import timber.log.Timber

class ComposeNavigator(
    private val navController: NavController
) : Navigator {

    override fun applyCommands(commands: Array<out Command>) {
        commands.forEach { command ->
           applyCommand(command)
        }
    }

    private fun applyCommand(command: Command) {
        when(command) {
            is Forward -> forward(command)
            is Replace -> replace(command)
            is BackTo -> backTo(command)
            is Back -> back()
        }
    }

    private fun back() {
        navController.popBackStack()
    }

    private fun backTo(command: BackTo) {
        when(val screen = command.screen) {
            null -> {
                navigateToRoot()
            }
            is Destination -> {
                val popped = navController.popBackStack(screen, inclusive = false)
                Timber.i("popped: $popped, currentDestination is $screen == ${navController.currentDestination?.hasRoute(screen::class)} ")
                if (!popped || navController.currentDestination?.hasRoute(screen::class) != true) {
                    navController.navigate(screen)
                }
            }
        }
    }

    private fun replace(command: Replace) {
        when(val screen = command.screen) {
            is Destination -> {
                val currentId = navController.currentDestination?.id ?: return
                navController.navigate(screen) {
                    popUpTo(currentId) {
                        inclusive = true
                    }
                }
                Timber.i("current startDestination: ${navController.graph.startDestinationRoute}")
            }
        }
    }

    private fun forward(command: Forward) {
        when(val screen = command.screen) {
            is Destination -> navController.navigate(screen)
        }
    }

    private fun navigateToRoot() {
        navController.popBackStack(navController.graph.startDestinationId, inclusive = false)
    }

}