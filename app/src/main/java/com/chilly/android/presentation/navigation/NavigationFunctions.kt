package com.chilly.android.presentation.navigation

import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import timber.log.Timber

fun NavController.clearStackAndNavigate(destination: Destination) {
    Timber.i("navigating to $destination")
    navigate(destination) {
        popUpTo(graph.findStartDestination().id) {
            inclusive = true
        }
    }
}

fun NavController.checkClearNavigate(destination: Destination, clear: Boolean) {
    if (clear) {
        clearStackAndNavigate(destination)
    } else {
        navigate(destination)
    }
}