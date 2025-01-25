package com.chilly.android.presentation.sign_up

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerSignUpComponent
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination

@Composable
private fun SignUpScreen() {

}

fun NavGraphBuilder.installSignUpComposable() {
    composable<Destination.SignUp> {
        ScreenHolder(
            componentFactory = {
                DaggerSignUpComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            SignUpScreen()
            NewsCollector(component.newsCollector)
        }
    }
}