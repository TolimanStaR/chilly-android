package com.chilly.android.presentation.screens.result

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class RecommendationResultUpdate @Inject constructor(

) : DslUpdate<RecommendationResultState, RecommendationResultEvent, RecommendationResultCommand, RecommendationResultNews>() {

    override fun NextBuilder.update(event: RecommendationResultEvent) = when (event) {
        is RecommendationResultEvent.UiEvent -> updateOnUi(event)
        is RecommendationResultEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: RecommendationResultEvent.UiEvent) {
        when (event) {
            RecommendationResultEvent.UiEvent.LoadAgainClicked -> {
                state { copy(errorOccurred = false) }
                commands(RecommendationResultCommand.LoadRecommendations)
            }
            RecommendationResultEvent.UiEvent.ScreenShown -> {
                commands(RecommendationResultCommand.LoadRecommendations)
            }
            RecommendationResultEvent.UiEvent.CheckRequest -> {
                commands(RecommendationResultCommand.CheckRequested)
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: RecommendationResultEvent.CommandEvent) {
        when (event) {
            RecommendationResultEvent.CommandEvent.LoadingFail -> {
                state { copy(errorOccurred = true) }
            }
            is RecommendationResultEvent.CommandEvent.LoadingSuccess -> {
                state { copy(recommendations = event.recommendations) }
            }

            RecommendationResultEvent.CommandEvent.ClearData -> {
                state {copy(recommendations = emptyList()) }
            }
        }
    }
}
