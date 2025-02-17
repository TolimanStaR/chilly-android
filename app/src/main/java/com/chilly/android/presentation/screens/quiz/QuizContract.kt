package com.chilly.android.presentation.screens.quiz

import kotlinx.serialization.Serializable

class QuizState(
    val questions: List<Int> = emptyList()
)

sealed interface QuizEvent {
    sealed interface UiEvent : QuizEvent {
        data object Clicked : UiEvent
    }

    sealed interface CommandEvent : QuizEvent {
        data object Success : CommandEvent
        data object Fail : CommandEvent
    }
}

@Serializable
enum class QuizType {
    MAIN, SHORT
}

sealed interface QuizCommand {
    data object Load : QuizCommand
}

sealed interface QuizNews {
    data object Navigate : QuizNews
}