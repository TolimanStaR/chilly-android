package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import kotlinx.coroutines.flow.Flow

interface CommentsRepository {

    // TODO use constants to make more readable
    suspend fun sendReview(request: CommentRequest): Result<SendResult>

    fun getComments(placeId: Int): Flow<List<CommentDto>>

    suspend fun fetchNextCommentsPage(placeId: Int): Result<FetchResult>

    sealed interface FetchResult {
        data object EmptyPage : FetchResult
        data object PageWithContent : FetchResult
    }

    sealed interface SendResult {
        data object ReviewCreated : SendResult
        data object ReviewModified : SendResult
    }
}