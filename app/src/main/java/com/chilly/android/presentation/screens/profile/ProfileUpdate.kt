package com.chilly.android.presentation.screens.profile

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class ProfileUpdate @Inject constructor(

) : DslUpdate<ProfileState, ProfileEvent, ProfileCommand, ProfileNews>() {

    override fun NextBuilder.update(event: ProfileEvent) = when (event) {
        is ProfileEvent.UiEvent -> updateOnUi(event)
        is ProfileEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: ProfileEvent.UiEvent) {
        when (event) {
            else -> Unit
        }
    }

    private fun NextBuilder.updateOnCommand(event: ProfileEvent.CommandEvent) {
        when (event) {
            else -> Unit
        }
    }
}
