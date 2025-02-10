package com.chilly.android.presentation.screens.main

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class MainUpdate @Inject constructor(
    
) : DslUpdate<MainState, MainEvent, MainCommand, MainNews>() {

    override fun NextBuilder.update(event: MainEvent) = when(event) {
        is MainEvent.UiEvent -> updateOnUi(event)
        is MainEvent.CommandEvent -> updateOnCommand(event)
    }
    
    private fun NextBuilder.updateOnUi(event: MainEvent.UiEvent) {
        when(event) {
            MainEvent.UiEvent.SignOutClicked -> news(MainNews.NavigateLogin)
            MainEvent.UiEvent.ToOnboardingClicked -> news(MainNews.NavigateOnboarding)
        }
    }
    
        private fun NextBuilder.updateOnCommand(event: MainEvent.CommandEvent) {
        when(event) {
            MainEvent.CommandEvent.Fail -> Unit
            MainEvent.CommandEvent.Success -> Unit
        }
    }
}
