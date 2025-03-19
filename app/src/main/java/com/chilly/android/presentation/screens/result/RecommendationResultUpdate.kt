package com.chilly.android.presentation.screens.result

import com.chilly.android.presentation.screens.result.RecommendationResultEvent.CommandEvent
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import timber.log.Timber
import javax.inject.Inject

class RecommendationResultUpdate @Inject constructor(

) : DslUpdate<RecommendationResultState, RecommendationResultEvent, RecommendationResultCommand, RecommendationResultNews>() {

    override fun NextBuilder.update(event: RecommendationResultEvent) = when (event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when (event) {
            UiEvent.LoadAgainClicked -> {
                state { copy(errorOccurred = false) }
                commands(RecommendationResultCommand.LoadRecommendations)
            }
            UiEvent.ScreenShown -> {
                commands(RecommendationResultCommand.LoadRecommendations)
            }
            UiEvent.CheckRequest -> {
                commands(RecommendationResultCommand.CheckRequested)
            }
            is UiEvent.PlaceClicked -> {
                news(RecommendationResultNews.NavigatePlace(event.id))
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.LoadingFail -> {
                state { copy(errorOccurred = true) }
            }
            is CommandEvent.LoadingSuccess -> {
                state { copy(recommendations = event.recommendations) }
            }

            CommandEvent.ClearData -> {
                Timber.e("result cleared")
                state {copy(recommendations = emptyList()) }
            }
        }
    }
}
