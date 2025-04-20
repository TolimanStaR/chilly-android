package com.chilly.android.presentation.screens.rating

import com.chilly.android.data.remote.dto.PlaceDto

data class RatingState(
    val placeIds: List<Int>,
    val places: List<PlaceDto> = emptyList(),
    val currentlyRatingPlace: PlaceDto? = null,
    val selectedRating: Float = 0f,
    val commentText: String = ""
)

sealed interface RatingEvent {
    sealed interface UiEvent : RatingEvent {
        data object DialogDismissed : UiEvent
        data object RatingSent : UiEvent
        data class RateClicked(val place: PlaceDto) : UiEvent
        data class CommentTextChanged(val value: String) : UiEvent
        data class RatingChanged(val value: Float) : UiEvent
    }

    sealed interface CommandEvent : RatingEvent {
        data class LoadSuccess(val places: List<PlaceDto>) : CommandEvent
        data object SentSuccess : CommandEvent
        data object Fail : CommandEvent
    }
}

sealed interface RatingCommand {
    data class Load(val ids: List<Int>) : RatingCommand
    data class SendRating(val placeId: Int, val rating: Float, val comment: String) : RatingCommand
}

sealed interface RatingNews {
    data object GeneralFail : RatingNews
    data object SentSuccess : RatingNews
}