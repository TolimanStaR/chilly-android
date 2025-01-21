package com.chilly.android.presentation.navigation

import com.github.terrakok.cicerone.Screen
import kotlinx.serialization.Serializable


sealed interface Destination : Screen {
    @Serializable
    data object Splash : Destination

    @Serializable
    data class Onboarding(val index: Int) : Destination

    @Serializable
    data object Main : Destination

    @Serializable
    data object LogIn : Destination
}
