package com.chilly.android.presentation.navigation

import com.chilly.android.presentation.screens.quiz.QuizType
import com.github.terrakok.cicerone.Screen
import kotlinx.serialization.Serializable


sealed interface Destination : Screen {
    @Serializable
    data object Splash : Destination

    @Serializable
    data class Onboarding(val index: Int = 0) : Destination

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
    data object History : Destination // TODO

    @Serializable
    data object Favorites : Destination // TODO

    @Serializable
    data class Quiz(val type: QuizType) : Destination

}
