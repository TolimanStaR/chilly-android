package com.chilly.android.data.repository

import com.chilly.android.data.local.dao.HistoryDao
import com.chilly.android.data.local.dao.PlaceDao
import com.chilly.android.data.local.entity.FavoriteItem
import com.chilly.android.data.local.entity.HistoryEntry
import com.chilly.android.data.mapper.HistoryMapper
import com.chilly.android.data.mapper.PlaceMapper
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.model.HistoryItem
import com.chilly.android.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

internal class PlaceRepositoryImpl(
    private val placeDao: PlaceDao,
    private val historyDao: HistoryDao,
    private val placeMapper: PlaceMapper,
    private val historyMapper: HistoryMapper
) : PlaceRepository {

    override suspend fun placeById(id: Int): Result<PlaceDto> {
        return placeDao.findById(id)
            ?.let(placeMapper::toDto)
            ?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException())
    }

    override suspend fun savePlaces(places: List<PlaceDto>) {
        val entitiesArray = places.map(placeMapper::toEntity).toTypedArray()
        placeDao.insertPlaces(*entitiesArray)

        val historyEntries = places.map { place ->
            HistoryEntry(
                placeId = place.id,
                timestamp = LocalDateTime.ofInstant(Instant.now(), ZoneId.systemDefault())
            )
        }.toTypedArray()
        historyDao.insertEntry(*historyEntries)
    }

    override suspend fun checkInFavorites(placeId: Int): Boolean {
        return placeDao.searchInFavorites(placeId) != null
    }

    override suspend fun updateFavorites(placeId: Int, isInFavorites: Boolean) {
        val item = FavoriteItem(placeId)
        if (isInFavorites) {
            placeDao.markAsFavorite(item)
        } else {
            placeDao.removeFromFavorites(item)
        }
    }

    override fun getHistoryFlow(): Flow<List<HistoryItem>> =
        historyDao.getHistory()
            .map { currentHistory ->
                currentHistory.map(historyMapper::toModel)
            }

    override fun getFavoritesFlow(): Flow<List<PlaceDto>> =
        placeDao.getFavorites()
            .map { favorites ->
                favorites.map(placeMapper::toDto)
            }

    override suspend fun clearHistory() {
        historyDao.clearHistory()
    }

    override suspend fun removeItem(item: HistoryItem) {
        val entity = historyMapper.toEntity(item)
        historyDao.removeHistoryEntry(entity)
    }
}