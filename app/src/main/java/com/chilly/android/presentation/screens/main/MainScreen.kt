package com.chilly.android.presentation.screens.main

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.applicationComponent
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination

@Composable
fun MainScreen(
    onExit: () -> Unit = {},
    onOnboarding: () -> Unit = {}
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(30.dp).fillMaxSize()
    ) {
        Text("Main screen")
        Button(
            onClick = onExit
        ) {
            Text("sign out")
        }
        Button(
            onClick = onOnboarding
        ) {
            Text("to onboarding")
        }
    }
}

fun NavGraphBuilder.mainScreenComposable(navController: NavController) {
    composable<Destination.Main> {
        ScreenHolder(
            viewModelFactory = {
                applicationComponent.mainViewModelFactory().build(navController::navigate)
            }
        ) {
            MainScreen(::onExitClicked, ::onShowOnboarding)
        }
    }
}