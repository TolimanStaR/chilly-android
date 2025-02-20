package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.QuestionDto
import com.chilly.android.data.remote.dto.QuizAnswerDto
import com.chilly.android.data.remote.dto.QuizType

interface QuizRepository {
    suspend fun getQuestions(type: QuizType): Result<List<QuestionDto>>
    suspend fun saveAnswers(type: QuizType, answers: List<QuizAnswerDto>): Result<Unit>
}