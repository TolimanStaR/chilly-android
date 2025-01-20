package com.chilly.android.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import com.chilly.android.presentation.login.logInScreenComposable
import com.chilly.android.presentation.main.mainScreenComposable
import com.chilly.android.presentation.onboarding.onboardingComposable
import com.chilly.android.presentation.splash.splashComposable

@Composable
fun ChillyNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(navController, Destination.Splash) {
        splashComposable(navController)
        onboardingComposable(navController)
        mainScreenComposable(navController)
        logInScreenComposable(navController)
    }
}