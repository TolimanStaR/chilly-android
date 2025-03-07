package com.chilly.android.presentation.screens.quiz

import com.chilly.android.data.remote.dto.QuizAnswerDto
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.presentation.screens.quiz.QuizEvent.CommandEvent
import com.chilly.android.presentation.screens.quiz.QuizEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class QuizUpdate @Inject constructor() : DslUpdate<QuizState, QuizEvent, QuizCommand, QuizNews>() {

    override fun NextBuilder.update(event: QuizEvent) = when (event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when (event) {
            is UiEvent.AnswerSelected -> {
                saveAnswer(event.index)
                state { copy(currentQuestion = (currentQuestion + 1).coerceAtMost(questions.lastIndex)) }
            }
            is UiEvent.LoadAgainClicked -> {
                commands(QuizCommand.LoadQuestions(event.type))
                state { copy(errorOccurred = false) }
            }
            is UiEvent.SendResultClicked -> {
                commands(QuizCommand.SendAnswers(event.type, state.answers))
                state { copy(isLoading = true) }
            }
            is UiEvent.ShowLoadingScreen -> {
                commands(QuizCommand.LoadQuestions(event.type))
            }

            UiEvent.BackPressed -> {
                if (state.currentQuestion <= 0) {
                    state { copy(questions = emptyList(), answers = emptyList(), errorOccurred = false) }
                    news(QuizNews.NavigateBack)
                } else {
                    state { copy(currentQuestion = currentQuestion - 1) }
                }
            }

            UiEvent.ForwardPressed -> {
                if (state.currentQuestion == state.answers.size) {
                    return
                }
                state { copy(currentQuestion = currentQuestion + 1) }
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.GeneralFail -> {
                state { copy(isLoading = false, errorOccurred = true) }
                news(QuizNews.GeneralFail)
            }
            is CommandEvent.QuestionsLoaded -> {
                state { copy(questions = event.questions, currentQuestion = 0) }
                state { copy(loadedQuizType = event.type) }
            }
            is CommandEvent.AnswersSent -> {
                state { copy(answers = emptyList(), questions = emptyList(), isLoading = false) }
                if (event.type == QuizType.BASE) {
                    commands(QuizCommand.MarkMainQuizFilled)
                    news(QuizNews.NavigateShortQuiz)
                } else {
                    news(QuizNews.NavigateToRecommendationResult)
                }
            }
        }
    }

    private fun NextBuilder.saveAnswer(index: Int) {
        val newAnswers = with(state) {
            val answerDto = with(questions[currentQuestion]) {
                QuizAnswerDto(id, answers[index].id)
            }

            answers.toMutableList().apply {
                if (currentQuestion < size) {
                    set(currentQuestion, answerDto)
                } else {
                    add(answerDto)
                }
            }
        }

        state { copy(answers = newAnswers, allAnswered = newAnswers.size == questions.size) }
    }
}
