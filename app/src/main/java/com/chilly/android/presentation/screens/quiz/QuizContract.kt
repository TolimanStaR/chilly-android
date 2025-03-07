package com.chilly.android.presentation.screens.quiz

import com.chilly.android.data.remote.dto.QuestionDto
import com.chilly.android.data.remote.dto.QuizAnswerDto
import com.chilly.android.data.remote.dto.QuizType

data class QuizState(
    val questions: List<QuestionDto> = emptyList(),
    val answers: List<QuizAnswerDto> = emptyList(),
    val currentQuestion: Int = -1,
    val errorOccurred: Boolean = false,
    val isLoading: Boolean = false,
    val allAnswered: Boolean = false,
    val loadedQuizType: QuizType? = null
)

sealed interface QuizEvent {
    sealed interface UiEvent : QuizEvent {
        data object BackPressed : UiEvent
        data object ForwardPressed : UiEvent
        data class ShowLoadingScreen(val type: QuizType) : UiEvent
        data class LoadAgainClicked(val type: QuizType) : UiEvent
        data class AnswerSelected(val index: Int) : UiEvent
        data class SendResultClicked(val type: QuizType) : UiEvent
    }

    sealed interface CommandEvent : QuizEvent {
        data class QuestionsLoaded(val questions: List<QuestionDto>, val type: QuizType) : CommandEvent
        data class AnswersSent(val type: QuizType) : CommandEvent
        data object GeneralFail : CommandEvent
    }
}

sealed interface QuizCommand {
    data class LoadQuestions(val type: QuizType) : QuizCommand
    data class SendAnswers(val type: QuizType, val answers: List<QuizAnswerDto>) : QuizCommand
    data object MarkMainQuizFilled : QuizCommand
}

sealed interface QuizNews {
    data object GeneralFail : QuizNews
    data object NavigateBack : QuizNews
    data object NavigateToRecommendationResult : QuizNews
    data object NavigateShortQuiz : QuizNews
}