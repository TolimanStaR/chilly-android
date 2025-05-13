package com.chilly.android.presentation.screens.main

import com.chilly.android.presentation.screens.main.MainEvent.CommandEvent
import com.chilly.android.presentation.screens.main.MainEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import timber.log.Timber
import javax.inject.Inject

class MainUpdate @Inject constructor(
) : DslUpdate<MainState, MainEvent, MainCommand, MainNews>() {

    override fun NextBuilder.update(event: MainEvent) = when(event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }
    
    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when(event) {
            // check whether person has completed main questionnaire if yes then go to to small questionnaire otherwise open full questionaire
            UiEvent.GetRecommendationClicked -> commands(MainCommand.CheckMainQuiz)
            UiEvent.LastFeedElementIsVisible -> {
                state { copy(isLoading = true) }
                commands(MainCommand.LoadNewFeedPage)
            }
            UiEvent.PulledToRefresh -> {
                state { copy(isRefreshing = true) }
                commands(MainCommand.RefreshFeed)
            }
            UiEvent.ScreenIsShown -> {
                state { copy(isLoading = true) }
                commands(MainCommand.LoadFeed, MainCommand.LoadNewFeedPage)
            }
            is UiEvent.PlaceClicked -> {
                news(MainNews.NavigatePlace(event.place))
            }
            is UiEvent.GotPermissionRequestResult -> {
                Timber.i("PERMISSIONS: result of request: ${event.permissions.entries.map { it.toPair() }}")
                val accepted = event.permissions.values.any { it }
                Timber.i("PERMISSIONS: ${if (accepted) "ACCEPTED" else "DENIED"}")
                if (!accepted) {
                    news(MainNews.PermissionsDenied)
                }
                state { copy(permissionsChecked = true, locationAccessGranted = accepted) }
            }
            UiEvent.PermissionsGranted -> {
                state { copy(permissionsChecked = true, locationAccessGranted = true) }
            }
        }
    }
    
    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when(event) {
            is CommandEvent.CheckQuizResult -> {
                if (event.hasBeenCompleted) {
                    news(MainNews.NavigateShortQuiz)
                } else {
                    news(MainNews.NavigateMainQuiz)
                }
            }
            CommandEvent.FeedUpdateFailed -> {
                state { copy(isLoading = false, isRefreshing = false) }
                news(MainNews.GeneralFail)
            }
            is CommandEvent.FeedUpdated -> {
                state { copy(feed = event.newFeed, isLoading = false, isRefreshing = false) }
            }

            CommandEvent.SameLocationRefresh -> {
                news(MainNews.SameLocationWasUsed)
            }
        }
    }
}
