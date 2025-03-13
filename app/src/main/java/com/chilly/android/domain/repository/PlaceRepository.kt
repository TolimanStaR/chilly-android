package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.PlaceDto

interface PlaceRepository {

    suspend fun placeById(id: Int): Result<PlaceDto>

    suspend fun savePlaces(places: List<PlaceDto>)
}