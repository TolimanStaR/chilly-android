package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.model.HistoryItem
import kotlinx.coroutines.flow.Flow

interface PlaceRepository {

    suspend fun placeById(id: Int): Result<PlaceDto>

    suspend fun savePlaces(places: List<PlaceDto>, writeToHistory: Boolean)

    suspend fun checkInFavorites(placeId: Int): Boolean

    suspend fun updateFavorites(placeId: Int, isInFavorites: Boolean)

    fun getHistoryFlow(): Flow<List<HistoryItem>>

    fun getFavoritesFlow(): Flow<List<PlaceDto>>

    suspend fun clearHistory()

    suspend fun removeItem(item: HistoryItem)
}