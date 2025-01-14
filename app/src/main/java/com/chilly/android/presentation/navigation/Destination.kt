package com.chilly.android.presentation.navigation

import kotlinx.serialization.Serializable


sealed interface Destination {
    @Serializable
    data object Splash : Destination

    @Serializable
    data class OnBoarding(val index: Int) : Destination

    @Serializable
    data object Main : Destination

    @Serializable
    data object LogIn : Destination
}
