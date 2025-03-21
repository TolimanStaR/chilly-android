package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.FeedApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.LocationRepository
import io.ktor.client.HttpClient
import io.ktor.client.request.parameter

class FeedApiImpl (
    private val client: HttpClient,
    private val tokenHolder: TokenHolder
) : FeedApi {

    override suspend fun getFeedPage(
        location: LocationRepository.LocationData,
        page: Int,
        pageSize: Int?
    ): Result<List<PlaceDto>> = client.getResult("api/places/nearby") {
        setAuthorization(tokenHolder)
        parameter("page", page)
        parameter("latitude", location.latitude)
        parameter("longitude", location.longitude)
        pageSize?.let { parameter("size", it) }
    }

}