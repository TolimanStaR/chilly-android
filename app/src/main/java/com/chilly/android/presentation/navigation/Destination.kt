package com.chilly.android.presentation.navigation

import com.chilly.android.data.remote.dto.QuizType
import com.github.terrakok.cicerone.Screen
import kotlinx.serialization.Serializable


sealed interface Destination : Screen {
    @Serializable
    data object Splash : Destination

    @Serializable
    data class Onboarding(val index: Int = 0, val loggedIn: Boolean = false) : Destination

    @Serializable
    data object Main : Destination

    @Serializable
    data object LogIn : Destination

    @Serializable
    data object SignUp : Destination

    @Serializable
    data object ForgotPassword : Destination

    @Serializable
    data object Profile : Destination

    @Serializable
    data object History : Destination

    @Serializable
    data object Favorites : Destination

    @Serializable
    data object RecommendationResult : Destination

    @Serializable
    data class Quiz(val type: QuizType) : Destination

    @Serializable
    data class PlaceInfo(val id: Int, val name: String) : Destination

    @Serializable
    data class Rating(val ids: List<Int>) : Destination

}
