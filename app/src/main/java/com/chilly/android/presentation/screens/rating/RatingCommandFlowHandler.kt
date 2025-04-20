package com.chilly.android.presentation.screens.rating

import com.chilly.android.data.remote.api.CommentsApi
import com.chilly.android.data.remote.dto.request.CommentRequest
import com.chilly.android.domain.repository.PlaceRepository
import com.chilly.android.presentation.screens.rating.RatingEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class RatingCommandFlowHandler @Inject constructor(
    private val placeRepository: PlaceRepository,
    private val commentsApi: CommentsApi
) : CommandsFlowHandler<RatingCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<RatingCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is RatingCommand.Load -> handleLoad(it)
                is RatingCommand.SendRating -> handleRating(it)
            }
        }

    private fun handleLoad(command: RatingCommand.Load): Flow<CommandEvent> = flow {
        val places = command.ids.mapNotNull { id ->
            placeRepository.placeById(id).getOrNull()
        }
        emit(CommandEvent.LoadSuccess(places))
    }

    private fun handleRating(command: RatingCommand.SendRating): Flow<CommandEvent> = flow {
        val event = commentsApi.sendComment(CommentRequest(
            placeId = command.placeId,
            rating = command.rating,
            commentText = command.comment.let { it.ifBlank { null } }
        )).map {
            CommandEvent.SentSuccess
        }.getOrDefault(CommandEvent.Fail)
        emit(event)
    }
}