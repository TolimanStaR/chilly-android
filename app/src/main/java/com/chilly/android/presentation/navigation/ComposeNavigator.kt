package com.chilly.android.presentation.navigation

import androidx.navigation.NavController
import com.github.terrakok.cicerone.Back
import com.github.terrakok.cicerone.BackTo
import com.github.terrakok.cicerone.Command
import com.github.terrakok.cicerone.Forward
import com.github.terrakok.cicerone.Navigator
import com.github.terrakok.cicerone.Replace

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
        navController.navigateUp()
    }

    private fun backTo(command: BackTo) {
        when(val screen = command.screen) {
            null -> {
                navigateToRoot()
            }
            is Destination -> navController.popBackStack(screen, inclusive = false)
        }
    }

    private fun replace(command: Replace) {
        when(val screen = command.screen) {
            is Destination -> {
                navController.navigate(screen) {
                    val currentId = navController.currentDestination?.id ?: return@navigate
                    popUpTo(currentId) {
                        inclusive = true
                    }
                }
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