package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.request.CommentRequest

interface CommentsApi {
    suspend fun sendComment(request: CommentRequest): Result<Unit>
}