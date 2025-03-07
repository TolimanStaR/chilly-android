package com.chilly.android.domain.useCase.recommendation

import com.chilly.android.data.remote.api.RecommendationApi
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.domain.repository.PreferencesRepository
import timber.log.Timber
import javax.inject.Inject

class GetRecommendationUseCase @Inject constructor(
    private val recommendationApi: RecommendationApi,
    private val placeRepository: PlaceRepository,
    private val preferencesRepository: PreferencesRepository
) {

    suspend operator fun invoke(): Result<List<PlaceDto>> {
        return recommendationApi.getRecommendation()
            .onSuccess {
                placeRepository.savePlaces(it)
                preferencesRepository.setRequestedRecommendation(false)
            }
            .onFailure {
                Timber.e(it)
            }
    }
}