package com.chilly.android.presentation.screens.quiz

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.presentation.screens.WithTheme

@Composable
@PreviewLightDark
@Preview(showBackground = true)
private fun PreviewQuizScreen() = WithTheme {
    QuizScreen(
        state = QuizState(),
        type = QuizType.BASE,
        padding = PaddingValues(),
        onEvent = {}
    )
}