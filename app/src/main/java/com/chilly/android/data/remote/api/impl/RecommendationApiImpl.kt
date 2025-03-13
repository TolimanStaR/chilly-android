package com.chilly.android.data.remote.api.impl

import com.chilly.android.data.remote.TokenHolder
import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.api.getResult
import com.chilly.android.data.remote.api.setAuthorization
import com.chilly.android.data.remote.dto.PlaceDto
import io.ktor.client.HttpClient

class RecommendationApiImpl(
    private val client: HttpClient,
    private val tokenHolder: TokenHolder
) : RecommendationApi {

    override suspend fun getRecommendation(): Result<List<PlaceDto>>
        = client.getResult("api/recs") {
            setAuthorization(tokenHolder)
        }
}