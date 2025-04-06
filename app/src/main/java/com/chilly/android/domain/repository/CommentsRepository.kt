package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    // TODO use constants to make more readable
    suspend fun sendReview(request: CommentRequest): Result<Boolean>

    fun getComments(placeId: Int): Flow<List<CommentDto>>

    suspend fun fetchNextCommentsPage(placeId: Int): Result<Boolean>

}