package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.PlaceDto
import kotlinx.coroutines.flow.Flow

interface FeedRepository {

    fun getFeedFlow(): Flow<List<PlaceDto>>

    suspend fun requestNextPage(): Result<Unit>

    suspend fun refreshFeed(): Result<Unit>

    class LocationNotAvailableException : Exception()
}