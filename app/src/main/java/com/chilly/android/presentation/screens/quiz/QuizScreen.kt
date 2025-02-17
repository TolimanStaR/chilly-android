package com.chilly.android.presentation.screens.quiz

import androidx.compose.runtime.Composable
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.applicationComponent
import com.chilly.android.di.screens.DaggerQuizComponent
import com.chilly.android.di.screens.QuizComponent
import com.chilly.android.presentation.theme.ChillyTheme
import com.chilly.android.presentation.navigation.Destination

@Composable
private fun QuizScreen(
    state: QuizState,
    onEvent: (QuizEvent.UiEvent) -> Unit
) {
}

fun NavGraphBuilder.installQuizScreen() {
    composable<Destination.Quiz> {
        ScreenHolder<QuizStore, QuizComponent>(
            componentFactory = {
                DaggerQuizComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            NewsCollector(component.newsCollector)
            QuizScreen(state.value, store::dispatch)
        }
    }
}

@Composable
@PreviewLightDark
@Preview(name = "QuizScreen", showSystemUi = true, showBackground = true)
private fun PreviewQuizScreen() {
    ChillyTheme {
        QuizScreen(
            state = QuizState(),
            onEvent = {}
        )
    }
}

