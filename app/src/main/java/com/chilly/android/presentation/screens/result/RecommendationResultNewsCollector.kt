package com.chilly.android.presentation.screens.result

import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class RecommendationResultNewsCollector @Inject constructor(
) : FlowCollector<RecommendationResultNews> {

    override suspend fun emit(value: RecommendationResultNews) {
        when (value) {
            else -> Unit
        }
    }
}