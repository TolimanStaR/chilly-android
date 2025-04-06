package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.postWithResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter
import io.ktor.client.request.setBody
import io.ktor.http.HttpStatusCode

class CommentApiImpl(
    private val client: HttpClient,
    private val tokenHolder: TokenHolder
) : CommentsApi {

    override suspend fun sendComment(request: CommentRequest): Result<Boolean> =
        client.postWithResult(
            "api/reviews",
            onResponse = {
                status == HttpStatusCode.Created
            }
        ) {
            setAuthorization(tokenHolder)
            setBody(request)
        }

    override suspend fun getCommentsPage(
        placeId: Int,
        page: Int,
        pageSize: Int?
    ): Result<List<CommentDto>> = client.getResult("api/reviews/place/$placeId") {
        setAuthorization(tokenHolder)
        parameter("page", page)
        pageSize?.let { parameter("size", it) }
    }
}