package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.request.CommentRequest
import com.chilly.android.domain.repository.CommentsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

class CommentsRepositoryImpl(
    private val commentsApi: CommentsApi
) : CommentsRepository {

    private var currentPlaceId: Int = -1
        set(value) {
            if (field != value) {
                commentsFlow = MutableStateFlow(emptyList())
                lastFetchedPage = -1
            }
            field = value
        }

    private var commentsFlow: MutableStateFlow<List<CommentDto>> = MutableStateFlow(emptyList())

    private var lastFetchedPage: Int = -1

    override suspend fun sendReview(request: CommentRequest): Result<Unit> {
        return commentsApi.sendComment(request)
    }

    override fun getComments(placeId: Int): Flow<List<CommentDto>> {
        currentPlaceId = placeId
        return commentsFlow
    }

    override suspend fun fetchNextCommentsPage(placeId: Int): Result<Unit> {
        currentPlaceId = placeId
        val fetchingPage = lastFetchedPage + 1
        return commentsApi.getCommentsPage(currentPlaceId, lastFetchedPage + 1)
            .map { newPage ->
                lastFetchedPage = fetchingPage
                commentsFlow.update { currentComments ->
                    currentComments.toMutableList().apply {
                        addAll(newPage)
                    }
                }
            }
    }
}