package com.chilly.android.presentation.screens.quiz

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class QuizUpdate @Inject constructor(

) : DslUpdate<QuizState, QuizEvent, QuizCommand, QuizNews>() {

    override fun NextBuilder.update(event: QuizEvent) = when (event) {
        is QuizEvent.UiEvent -> updateOnUi(event)
        is QuizEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: QuizEvent.UiEvent) {
        when (event) {
            else -> Unit
        }
    }

    private fun NextBuilder.updateOnCommand(event: QuizEvent.CommandEvent) {
        when (event) {
            else -> Unit
        }
    }
}
