package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.data.remote.dto.request.QuizResponseRequest
import com.chilly.android.data.remote.dto.response.QuestionsResponse

interface QuizApi {
    suspend fun getQuestions(type: QuizType): Result<QuestionsResponse>
    suspend fun saveAnswers(request: QuizResponseRequest): Result<Unit>
}