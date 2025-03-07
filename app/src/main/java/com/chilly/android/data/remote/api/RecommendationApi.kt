package com.chilly.android.data.remote.api

import com.chilly.android.data.remote.dto.PlaceDto

interface RecommendationApi {
    suspend fun getRecommendation(): Result<List<PlaceDto>>
}