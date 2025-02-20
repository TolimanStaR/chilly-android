package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.dto.QuestionDto
import com.chilly.android.data.remote.dto.QuizAnswerDto
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.data.remote.dto.request.QuizResponseRequest
import com.chilly.android.domain.repository.QuizRepository

class QuizRepositoryImpl(
    private val quizApi: QuizApi
) : QuizRepository {

    private val cachedQuestions: MutableMap<QuizType, List<QuestionDto>> = mutableMapOf()

    override suspend fun getQuestions(type: QuizType): Result<List<QuestionDto>> {
        return cachedQuestions[type]?.let { Result.success(it) } ?:
            quizApi.getQuestions(type)
                .map { it.questions }
                .onSuccess { cachedQuestions[type] = it }

    }

    override suspend fun saveAnswers(type: QuizType, answers: List<QuizAnswerDto>): Result<Unit> {
        return quizApi.saveAnswers(QuizResponseRequest(type.name, answers))
    }
}