package com.chilly.android.presentation.screens.profile

class ProfileState(
    val text: String = ""
)

sealed interface ProfileEvent {
    sealed interface UiEvent : ProfileEvent {
        data object Clicked : UiEvent
    }

    sealed interface CommandEvent : ProfileEvent {
        data object Success : CommandEvent
        data object Fail : CommandEvent
    }
}

sealed interface ProfileCommand {
    data object Load : ProfileCommand
}

sealed interface ProfileNews {
    data object Navigate : ProfileNews
}