package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.QuizApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.api.wrappedPost
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.data.remote.dto.request.QuizResponseRequest
import com.chilly.android.data.remote.dto.response.QuestionsResponse
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody

class QuizApiImpl(
    private val client: HttpClient,
    private val tokenHolder: TokenHolder
) : QuizApi {

    override suspend fun getQuestions(type: QuizType): Result<QuestionsResponse> =
        client.getResult("api/questions") {
            setAuthorization(tokenHolder)
            parameter("type", type.name)
        }

    override suspend fun saveAnswers(request: QuizResponseRequest): Result<Unit> =
        client.wrappedPost("api/quiz") {
            setAuthorization(tokenHolder)
            setBody(request)
        }
}