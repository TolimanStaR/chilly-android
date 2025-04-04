package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import io.ktor.client.HttpClient

// TODO use real API
class CommentApiImpl(
    private val client: HttpClient,
    private val tokenHolder: TokenHolder
) : CommentsApi {

    override suspend fun sendComment(request: CommentRequest): Result<Unit> {
        if (request.rating > 3) return Result.success(Unit)
        return Result.failure(Exception())
    }

    override suspend fun getCommentsPage(
        placeId: Int,
        page: Int,
        pageSize: Int?
    ): Result<List<CommentDto>> {
        return Result.failure(Exception())
    }
}