package com.chilly.android.presentation.screens.place

import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class PlaceInfoUpdate @Inject constructor(

) : DslUpdate<PlaceInfoState, PlaceInfoEvent, PlaceInfoCommand, PlaceInfoNews>() {

    override fun NextBuilder.update(event: PlaceInfoEvent) = when (event) {
        is PlaceInfoEvent.UiEvent -> updateOnUi(event)
        is PlaceInfoEvent.CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: PlaceInfoEvent.UiEvent) {
        when (event) {
            PlaceInfoEvent.UiEvent.ReloadPlace -> {
                state { copy(errorOccurred = false) }
                commands(PlaceInfoCommand.LoadPlace(state.placeId))
            }
            PlaceInfoEvent.UiEvent.ShownLoading -> {
                state { copy(errorOccurred = false) }
                commands(PlaceInfoCommand.LoadPlace(state.placeId))
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: PlaceInfoEvent.CommandEvent) {
        when (event) {
            PlaceInfoEvent.CommandEvent.LoadFail -> {
                state { copy(errorOccurred = true) }
            }
            is PlaceInfoEvent.CommandEvent.LoadSuccess -> {
                state { copy(place = event.place) }
            }
        }
    }
}
