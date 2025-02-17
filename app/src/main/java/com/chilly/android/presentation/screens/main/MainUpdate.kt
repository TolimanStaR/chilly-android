package com.chilly.android.presentation.screens.main

import com.chilly.android.presentation.screens.main.MainEvent.CommandEvent
import com.chilly.android.presentation.screens.main.MainEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class MainUpdate @Inject constructor(
    
) : DslUpdate<Unit, MainEvent, MainCommand, MainNews>() {

    override fun NextBuilder.update(event: MainEvent) = when(event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }
    
    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when(event) {
            // check whether person has completed main questionnaire if yes then go to to small questionnaire otherwise open full questionaire
            UiEvent.GetRecommendationClicked -> commands(MainCommand.CheckMainQuiz)
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
        }
    }
}
