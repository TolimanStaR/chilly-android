package com.chilly.android.presentation.screens.quiz

import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class QuizNewsCollector @Inject constructor(
) : FlowCollector<QuizNews> {

    override suspend fun emit(value: QuizNews) {
        when (value) {
            else -> Unit
        }
    }
}