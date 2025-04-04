package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest

interface CommentsApi {
    suspend fun sendComment(request: CommentRequest): Result<Unit>

    suspend fun getCommentsPage(placeId: Int, page: Int = 0, pageSize: Int? = null): Result<List<CommentDto>>
}