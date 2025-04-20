package com.chilly.android.presentation.screens.place

import android.content.res.Resources
import com.chilly.android.data.remote.dto.CommentDto
import com.chilly.android.di.screens.PlaceInfoScope
import com.chilly.android.presentation.common.structure.UiStateMapper
import com.chilly.android.presentation.common.logic.formattedDate
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import javax.inject.Inject

@PlaceInfoScope
class PlaceUiMapper @Inject constructor() : UiStateMapper<PlaceInfoState, PlaceUiState> {

    override suspend fun Resources.mapToUiState(state: PlaceInfoState) = PlaceUiState(
        placeId = state.placeId,
        place = state.place,
        isInFavorites = state.isInFavorites,
        errorOccurred = state.errorOccurred,
        expandedSections = state.expandedSections,
        commentText = state.commentText,
        ratingValue = state.ratingValue,
        comments = mapComments(state.comments),
        allCommentsLoaded = state.allCommentsLoaded,
        isLoading = state.isLoading
    )

    private fun Resources.mapComments(comments: List<CommentDto>): List<CommentUiModel> {
        return comments.map { comment ->
            CommentUiModel(
                id = comment.id,
                timeString = timestampToString(comment.timestamp),
                text = comment.text,
                rating = comment.rating
            )
        }
    }

    private fun Resources.timestampToString(value: Long): String {
        val localDate = LocalDateTime.ofInstant(Instant.ofEpochMilli(value), ZoneId.systemDefault())
        return formattedDate(localDate)
    }
}
