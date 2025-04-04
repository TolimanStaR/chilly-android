package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    suspend fun sendReview(request: CommentRequest): Result<Unit>

    fun getComments(placeId: Int): Flow<List<CommentDto>>

    suspend fun fetchNextCommentsPage(placeId: Int): Result<Unit>

}