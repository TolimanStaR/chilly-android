package com.chilly.android.presentation.screens.profile

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerProfileComponent
import com.chilly.android.di.screens.ProfileComponent
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination

@Composable
private fun ProfileScreen(
    state: ProfileState,
    onEvent: (ProfileEvent.UiEvent) -> Unit
) {
}

fun NavGraphBuilder.installProfileScreen() {
    composable<Destination.Profile> {
        ScreenHolder<ProfileStore, ProfileComponent>(
            componentFactory = {
                DaggerProfileComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            ProfileScreen(state.value, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "ProfileScreen", showSystemUi = true, showBackground = true)
private fun PreviewProfileScreen() {
    ChillyTheme {
        ProfileScreen(
            state = ProfileState(),
            onEvent = {}
        )
    }
}

