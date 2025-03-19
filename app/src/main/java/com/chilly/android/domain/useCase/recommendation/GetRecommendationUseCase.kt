package com.chilly.android.domain.useCase.recommendation

import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.RecommendationRepository
import javax.inject.Inject

class GetRecommendationUseCase @Inject constructor(
    private val recommendationRepository: RecommendationRepository
) {

    suspend operator fun invoke(): Result<List<PlaceDto>> {
        return recommendationRepository.getRecommendation()
    }
}