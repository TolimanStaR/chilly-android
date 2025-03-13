package com.chilly.android.presentation.screens.quiz

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import com.chilly.android.R
import com.chilly.android.applicationComponent
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.di.screens.DaggerQuizComponent
import com.chilly.android.di.screens.QuizComponent
import com.chilly.android.presentation.common.components.ChillyButton
import com.chilly.android.presentation.common.components.ChillyButtonColor
import com.chilly.android.presentation.common.components.ChillyButtonType
import com.chilly.android.presentation.common.components.ErrorReloadPlaceHolder
import com.chilly.android.presentation.common.components.LoadingPlaceholder
import com.chilly.android.presentation.common.components.SizeParameter
import com.chilly.android.presentation.common.components.TextInDialogWindow
import com.chilly.android.presentation.common.components.chillyPepperRes
import com.chilly.android.presentation.common.structure.NewsCollector
import com.chilly.android.presentation.common.structure.ScreenHolder
import com.chilly.android.presentation.common.structure.collectState
import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.quiz.QuizEvent.UiEvent
import com.chilly.android.presentation.theme.ChillyTheme

@Composable
private fun QuizScreen(
    state: QuizState,
    type: QuizType,
    padding: PaddingValues,
    onEvent: (UiEvent) -> Unit
) {
    val needLoading = state.questions.isEmpty() || state.loadedQuizType != type
    when {
        needLoading && !state.errorOccurred -> {
            LoadingPlaceholder(stringResource(R.string.loading_questions_text)) {
                onEvent(UiEvent.ShowLoadingScreen(type))
            }
            return
        }
        needLoading && state.errorOccurred -> {
            ErrorReloadPlaceHolder(stringResource(R.string.error_occurred_during_loading)) {
                onEvent(UiEvent.LoadAgainClicked(type))
            }
            return
        }
    }

    BackHandler {
        onEvent(UiEvent.BackPressed)
    }

    val question = remember(state.questions, state.currentQuestion) {
        state.questions[state.currentQuestion]
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = Modifier
            .padding(padding)
            .padding(16.dp)
            .fillMaxSize()
    ) {
        // progress bar
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                IconButton(
                    onClick = { onEvent(UiEvent.BackPressed) },
                    enabled = state.currentQuestion > 0
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = null
                    )
                }
                Text(
                    text = stringResource(
                        R.string.quiz_progress_title,
                        state.currentQuestion + 1, state.questions.size
                    )
                )
                IconButton(
                    onClick = { onEvent(UiEvent.ForwardPressed) },
                    enabled = state.currentQuestion < minOf(state.questions.lastIndex, state.answers.size)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = null
                    )
                }
            }
            LinearProgressIndicator(
                progress = { state.answers.size.toFloat() / state.questions.size },
                gapSize = 0.dp,
                strokeCap = StrokeCap.Butt,
                drawStopIndicator = {},
                modifier = Modifier
                    .fillMaxWidth()
                    .height(32.dp)
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.secondary,
                        shape = RoundedCornerShape(8.dp)
                    )
                    .clip(RoundedCornerShape(8.dp))

            )
        }

        // Question with pepper image
        ElevatedCard(
            colors = CardDefaults.elevatedCardColors(
                containerColor = MaterialTheme.colorScheme.surface
            ),
            elevation = CardDefaults.cardElevation(4.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
        ) {
            Column(
                modifier = Modifier.padding(16.dp)
                    .fillMaxWidth()
            ) {
                TextInDialogWindow(
                    text = question.body,
                )
                Image(
                    painter = painterResource(state.currentQuestion.chillyPepperRes()),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxHeight()
                        .align(Alignment.End)
                )
            }
        }

        // list of options
        Column(
            modifier = Modifier
                .padding(16.dp)
        ) {
            question.answers.forEachIndexed { index, answer ->
                val alreadySelected = state.answers.size > state.currentQuestion &&
                    state.answers[state.currentQuestion].answerId == answer.id

                ChillyButton(
                    text = answer.body,
                    type = ChillyButtonType.Secondary,
                    color = if (alreadySelected) ChillyButtonColor.Primary else ChillyButtonColor.Gray,
                    onClick =  {
                        onEvent(UiEvent.AnswerSelected(index))
                    },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        // finish button
        if (state.currentQuestion == state.questions.lastIndex) {
            ChillyButton(
                textRes = R.string.finish_quiz_button,
                size = SizeParameter.Medium,
                onClick = {
                    onEvent(UiEvent.SendResultClicked(type))
                },
                enabled = state.allAnswered,
                modifier = Modifier
                    .fillMaxWidth()
            )
        }

        if (state.isLoading) {
            CircularProgressIndicator()
        }
    }
}




fun NavGraphBuilder.installQuizScreen(padding: PaddingValues) {
    composable<Destination.Quiz> { backStackEntry ->
        ScreenHolder<QuizStore, QuizComponent>(
            componentFactory = {
                DaggerQuizComponent.builder()
                    .appComponent(applicationComponent)
                    .build()
            },
            storeFactory = { store() }
        ) {
            val state = collectState()
            val route = backStackEntry.toRoute<Destination.Quiz>()
            QuizScreen(state.value, route.type, padding, store::dispatch)
            NewsCollector(component.newsCollector)
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
            type = QuizType.BASE,
            padding = PaddingValues(),
            onEvent = {}
        )
    }
}

