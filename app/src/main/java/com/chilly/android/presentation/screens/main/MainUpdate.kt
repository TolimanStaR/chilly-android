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
                Timber.i("lastItem visible event received")
            }
            UiEvent.PulledToRefresh -> {
                Timber.i("pulled to refresh event received")
            }
            UiEvent.ScreenIsShown -> {
                Timber.i("screen shown event received")
                commands(MainCommand.LoadFeed, MainCommand.LoadNewFeedPage)
                state { copy(isLoading = true) }
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
                state { copy(isLoading = false) }
                news(MainNews.GeneralFail)
            }
            is CommandEvent.FeedUpdated -> {
                state { copy(feed = event.newFeed, isLoading = false) }
            }
        }
    }
}
