package com.chilly.android.domain.repository

import com.chilly.android.data.remote.dto.PlaceDto

interface RecommendationRepository {

    suspend fun getRecommendation(): Result<List<PlaceDto>>
}