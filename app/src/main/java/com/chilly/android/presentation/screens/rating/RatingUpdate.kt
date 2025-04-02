package com.chilly.android.presentation.screens.rating

import com.chilly.android.presentation.screens.rating.RatingEvent.CommandEvent
import com.chilly.android.presentation.screens.rating.RatingEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class RatingUpdate @Inject constructor(

) : DslUpdate<RatingState, RatingEvent, RatingCommand, RatingNews>() {

    override fun NextBuilder.update(event: RatingEvent) = when (event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when (event) {
            UiEvent.DialogDismissed -> {
                state { copy(currentlyRatingPlace = null) }
            }
            is UiEvent.RateClicked -> {
                state { copy(currentlyRatingPlace = event.place)}
            }
            is UiEvent.CommentTextChanged -> {
                state { copy(commentText = event.value) }
            }
            is UiEvent.RatingChanged -> {
                state { copy(selectedRating = event.value) }
            }
            UiEvent.RatingSent -> {
                with(state) {
                    if (currentlyRatingPlace == null) return@with
                    commands(RatingCommand.SendRating(currentlyRatingPlace.id, selectedRating, commentText))
                }
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.Fail -> {
                news(RatingNews.GeneralFail)
            }
            is CommandEvent.LoadSuccess -> {
                state { copy(places = event.places) }
            }
            CommandEvent.SentSuccess -> {
                news(RatingNews.SentSuccess)
            }
        }
    }
}
