package com.chilly.android.data.repository

import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.PlaceRepository

internal class PlaceRepositoryImpl : PlaceRepository {

    private val cachedPlaces: MutableMap<Int, PlaceDto> = mutableMapOf()

    override suspend fun placeById(id: Int): Result<PlaceDto> {
        return cachedPlaces[id]?.let { Result.success(it) }
            ?: Result.failure(NoSuchElementException())
    }

    override suspend fun savePlaces(places: List<PlaceDto>) {
        places.forEach { place -> cachedPlaces[place.id] = place }
    }
}