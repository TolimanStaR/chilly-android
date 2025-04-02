package com.chilly.android.presentation.screens.place

import com.chilly.android.presentation.screens.place.PlaceInfoEvent.CommandEvent
import com.chilly.android.presentation.screens.place.PlaceInfoEvent.UiEvent
import ru.tinkoff.kotea.core.dsl.DslUpdate
import javax.inject.Inject

class PlaceInfoUpdate @Inject constructor(

) : DslUpdate<PlaceInfoState, PlaceInfoEvent, PlaceInfoCommand, PlaceInfoNews>() {

    override fun NextBuilder.update(event: PlaceInfoEvent) = when (event) {
        is UiEvent -> updateOnUi(event)
        is CommandEvent -> updateOnCommand(event)
    }

    private fun NextBuilder.updateOnUi(event: UiEvent) {
        when (event) {
            UiEvent.ReloadPlace -> {
                state { copy(errorOccurred = false) }
                commands(PlaceInfoCommand.LoadPlace(state.placeId), PlaceInfoCommand.CheckFavorite(state.placeId))
            }
            UiEvent.ShownLoading -> {
                state { copy(errorOccurred = false) }
                commands(PlaceInfoCommand.LoadPlace(state.placeId), PlaceInfoCommand.CheckFavorite(state.placeId))
            }
            UiEvent.ToggleFavoriteClicked -> {
                commands(PlaceInfoCommand.ToggleFavorites(state.placeId, state.isInFavorites))
            }
            is UiEvent.ToggleExpansion -> {
                val newSet = state.expandedSections.toMutableSet()
                val toggled = event.section
                if (toggled in newSet) {
                    newSet.remove(toggled)
                } else {
                    newSet.add(toggled)
                }
                state { copy(expandedSections = newSet) }
            }
            UiEvent.BackClicked -> {
                news(PlaceInfoNews.NavigateUp)
            }

            is UiEvent.CommentTextChanged -> {
                state { copy(commentText = event.value) }
            }
            UiEvent.EmptyReviewsSectionExpanded -> {
                commands(PlaceInfoCommand.LoadComments(state.placeId))
            }
            UiEvent.LoadNextCommentsPageClicked -> {
                commands(PlaceInfoCommand.LoadComments(state.placeId))
            }
            is UiEvent.RatingChanged -> {
                state { copy(ratingValue = event.value) }
            }
            UiEvent.SendRatingClicked -> {
                with(state) {
                    commands(PlaceInfoCommand.SendRating(placeId, ratingValue, commentText))
                }
            }
        }
    }

    private fun NextBuilder.updateOnCommand(event: CommandEvent) {
        when (event) {
            CommandEvent.LoadFail -> {
                state { copy(errorOccurred = true) }
            }
            is CommandEvent.LoadSuccess -> {
                state { copy(place = event.place) }
            }
            is CommandEvent.FavoritesCheckResult -> {
                state { copy(isInFavorites = event.inFavorites) }
            }

            is CommandEvent.CommentsLoaded -> {
                state { copy(comments = event.comments) }
            }
            CommandEvent.RatingSentSuccessfully -> {
                news(PlaceInfoNews.RatingSent)
            }
        }
    }
}
