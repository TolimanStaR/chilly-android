package com.chilly.android.presentation.screens.result

import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkRequest
import androidx.work.workDataOf
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.CommandEvent
import com.chilly.android.presentation.screens.result.RecommendationResultEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import java.util.concurrent.TimeUnit
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
                news(RecommendationResultNews.NavigatePlace(event.place))
            }

            is UiEvent.OnPermissionRequestResult -> {
                state { copy(isPermissionGranted = event.isGranted) }
            }
            UiEvent.PermissionGranted -> {
                state { copy(isPermissionGranted = true) }
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
                news(RecommendationResultNews.SubmitNotificationRequest(
                    createNotificationRequest(event.recommendations)
                ))
            }

            CommandEvent.ClearData -> {
                state {copy(recommendations = emptyList()) }
            }
        }
    }

    private fun createNotificationRequest(recs: List<PlaceDto>): WorkRequest {
        return OneTimeWorkRequestBuilder<NotificationWork>()
            .setInitialDelay(20, TimeUnit.HOURS)
            .setInputData(workDataOf(
                NotificationWork.INPUT_PLACE_IDS to recs.map { it.id }.toIntArray()
            ))
            .build()
    }
}
