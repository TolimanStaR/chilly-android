package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.LocationRepository

interface FeedApi {

    suspend fun getFeedPage(
        location: LocationRepository.LocationData,
        page: Int,
        pageSize: Int? = null
    ) : Result<List<PlaceDto>>
}