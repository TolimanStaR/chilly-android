package com.chilly.android.presentation.screens.main

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerMainComponent
import com.chilly.android.di.screens.MainComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.components.TextInDialogWindow
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.main.MainEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun MainScreen(
    scaffoldPadding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    Column (
        verticalArrangement = Arrangement.Bottom,
        modifier = Modifier
            .padding(scaffoldPadding)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        TextInDialogWindow(
            text = stringResource(R.string.main_pepper_dialog_text),
            verticalPaddingScale = 2f,
            horizontalPaddingScale = 2f
        )
        Spacer(modifier = Modifier.height(16.dp))
        Image(
            painter = painterResource(R.drawable.chilly_main_pepper),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))
        ChillyButton(
            textRes = R.string.app_name,
            onClick = { onEvent(UiEvent.GetRecommendationClicked) },
            size = SizeParameter.Medium,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

fun NavGraphBuilder.installMainScreen(padding: PaddingValues) {
    composable<Destination.Main> {
        ScreenHolder<MainStore, MainComponent>(
            componentFactory = {
                DaggerMainComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            NewsCollector(component.newsCollector)
            MainScreen(padding, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "MainScreen", showSystemUi = true, showBackground = true)
private fun PreviewMainScreen() {
    ChillyTheme {
        MainScreen(
            PaddingValues(),
            onEvent = {}
        )
    }
}

