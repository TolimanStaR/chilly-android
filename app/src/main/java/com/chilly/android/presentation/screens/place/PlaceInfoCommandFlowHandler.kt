package com.chilly.android.presentation.screens.place

import com.chilly.android.data.remote.dto.request.CommentRequest
import com.chilly.android.domain.repository.CommentsRepository
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.presentation.screens.place.PlaceInfoEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class PlaceInfoCommandFlowHandler @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val commentsRepository: CommentsRepository
) : CommandsFlowHandler<PlaceInfoCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<PlaceInfoCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is PlaceInfoCommand.LoadPlace -> handleLoad(it)
                is PlaceInfoCommand.CheckFavorite -> handleFavoriteCheck(it)
                is PlaceInfoCommand.ToggleFavorites -> handleFavoritesToggle(it)
                is PlaceInfoCommand.LoadComments -> handleComments(it)
                is PlaceInfoCommand.LoadCommentsPage -> handleCommentsPage(it)
                is PlaceInfoCommand.SendRating -> handleRatingSent(it)
            }
        }

    private fun handleFavoritesToggle(command: PlaceInfoCommand.ToggleFavorites): Flow<CommandEvent> = flow {
        val newStatus = !command.currentlyInFavorites
        placeRepository.updateFavorites(command.placeId, newStatus)
        emit(CommandEvent.FavoritesCheckResult(newStatus))
    }

    private fun handleComments(command: PlaceInfoCommand.LoadComments): Flow<CommandEvent> =
        commentsRepository.getComments(command.placeId)
            .map { CommandEvent.CommentsLoaded(it) }

    private fun handleCommentsPage(command: PlaceInfoCommand.LoadCommentsPage): Flow<CommandEvent> = flow {
        commentsRepository.fetchNextCommentsPage(command.placeId)
            .onSuccess { hasContent ->
                if (!hasContent) {
                    emit(CommandEvent.EmptyCommentPage)
                }
            }
            .onFailure {
                emit(CommandEvent.LoadFail)
            }
    }

    private fun handleRatingSent(command: PlaceInfoCommand.SendRating): Flow<CommandEvent> = flow {
        commentsRepository.sendReview(
            CommentRequest(
                placeId = command.placeId,
                rating = command.rating,
                commentText = command.comment.ifEmpty { null }
            )
        ).onSuccess { isNewComment ->
            if (!isNewComment) {
                emit(CommandEvent.CommentModified)
            }
        }.onFailure {
            emit(CommandEvent.LoadFail)
        }
    }

    private fun handleLoad(command: PlaceInfoCommand.LoadPlace): Flow<CommandEvent> = flow {
        val event = placeRepository.placeById(command.id)
            .map { CommandEvent.LoadSuccess(it) }
            .getOrDefault(CommandEvent.LoadFail)

        emit(event)
    }

    private fun handleFavoriteCheck(command: PlaceInfoCommand.CheckFavorite): Flow<CommandEvent> = flow {
        val checkResult = placeRepository.checkInFavorites(command.placeId)
        emit(CommandEvent.FavoritesCheckResult(checkResult))
    }
}