package com.chilly.android.presentation.screens.place

import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.data.remote.dto.PlaceDto

data class PlaceInfoState(
    val placeId: Int,
    val place: PlaceDto? = null,
    val isInFavorites: Boolean = false,
    val errorOccurred: Boolean = false,
    val expandedSections: Set<Section> = emptySet(),
    val commentText: String = "",
    val ratingValue: Float = 0f,
    val comments: List<CommentDto> = emptyList(),
    val allCommentsLoaded: Boolean = false,
    val isLoading: Boolean = false
)

data class PlaceUiState(
    val placeId: Int,
    val place: PlaceDto? = null,
    val isInFavorites: Boolean = false,
    val errorOccurred: Boolean = false,
    val expandedSections: Set<Section> = emptySet(),
    val commentText: String = "",
    val ratingValue: Float = 0f,
    val comments: List<CommentUiModel> = emptyList(),
    val allCommentsLoaded: Boolean = false,
    val isLoading: Boolean = false
)

class CommentUiModel(
    val id: Int,
    val timeString: String,
    val text: String?,
    val rating: Float
)

sealed interface PlaceInfoEvent {
    sealed interface UiEvent : PlaceInfoEvent {
        data object ShownLoading : UiEvent
        data object ReloadPlace : UiEvent
        data object ToggleFavoriteClicked : UiEvent
        data class ToggleExpansion(val section: Section) : UiEvent
        data object BackClicked : UiEvent
        data class CommentTextChanged(val value: String) : UiEvent
        data class RatingChanged(val value: Float) : UiEvent
        data object SendRatingClicked : UiEvent
        data object EmptyReviewsSectionExpanded : UiEvent
        data object LoadNextCommentsPageClicked : UiEvent
    }

    sealed interface CommandEvent : PlaceInfoEvent {
        data class LoadSuccess(val place: PlaceDto) : CommandEvent
        data object LoadFail : CommandEvent
        data class FavoritesCheckResult(val inFavorites: Boolean) : CommandEvent
        data object RatingSentSuccessfully : CommandEvent
        data class CommentsLoaded(val comments: List<CommentDto>) : CommandEvent
        data object EmptyCommentPage : CommandEvent
        data object CommentModified : CommandEvent
    }
}

sealed interface PlaceInfoCommand {
    data class LoadPlace(val id: Int) : PlaceInfoCommand
    data class CheckFavorite(val placeId: Int): PlaceInfoCommand
    data class ToggleFavorites(val placeId: Int, val currentlyInFavorites: Boolean) : PlaceInfoCommand
    data class SendRating(val placeId: Int, val rating: Float, val comment: String) : PlaceInfoCommand
    data class LoadComments(val placeId: Int) : PlaceInfoCommand
    data class LoadCommentsPage(val placeId: Int) : PlaceInfoCommand
}

sealed interface PlaceInfoNews {
    data object NavigateUp : PlaceInfoNews
    data object GeneralFail : PlaceInfoNews
    data object RatingSent : PlaceInfoNews
    data object EmptyCommentsLoaded : PlaceInfoNews
    data object CommentModified : PlaceInfoNews
}

enum class Section {
    CONTACTS,
    OPEN_HOURS,
    COMMENTS
}