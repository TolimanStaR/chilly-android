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
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerMainComponent
import com.chilly.android.di.screens.MainComponent
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination

@Composable
private fun MainScreen(
    state: MainState,
    onEvent: (MainEvent.UiEvent) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.spacedBy(10.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(30.dp).fillMaxSize()
    ) {
        Text("Main screen")
        Button(
            onClick = { onEvent(MainEvent.UiEvent.SignOutClicked) }
        ) {
            Text("sign out")
        }
        Button(
            onClick = { onEvent(MainEvent.UiEvent.ToOnboardingClicked ) }
        ) {
            Text("to onboarding")
        }
    }
}

fun NavGraphBuilder.installMainScreen() {
    composable<Destination.Main> {
        ScreenHolder<MainStore, MainComponent>(
            componentFactory = {
                DaggerMainComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            MainScreen(state.value, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "MainScreen", showSystemUi = true, showBackground = true)
private fun PreviewMainScreen() {
    ChillyTheme {
        MainScreen(
            state = MainState(),
            onEvent = {}
        )
    }
}

