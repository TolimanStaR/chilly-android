package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.RecommendationRepository

class RecommendationRepositoryImpl constructor(
    private val recommendationApi: RecommendationApi,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository
) : RecommendationRepository {

    private var cachedRecommendation: List<PlaceDto>? = null

    override suspend fun getRecommendation(): Result<List<PlaceDto>> {
        val currentRecommendation = cachedRecommendation
        if (!preferencesRepository.hasRequestedRecommendation() && currentRecommendation != null) {
            return Result.success(currentRecommendation)
        }

        return recommendationApi.getRecommendation()
            .onSuccess {
                placeRepository.savePlaces(it, writeToHistory = true)
                cachedRecommendation = it
                preferencesRepository.setRequestedRecommendation(false)
            }
    }


}