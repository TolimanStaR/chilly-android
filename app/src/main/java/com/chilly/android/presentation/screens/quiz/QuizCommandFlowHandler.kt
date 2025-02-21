package com.chilly.android.presentation.screens.quiz

import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.domain.repository.PreferencesRepository
import com.chilly.android.domain.repository.QuizRepository
import com.chilly.android.presentation.screens.quiz.QuizEvent.CommandEvent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapMerge
import kotlinx.coroutines.flow.flow
import ru.tinkoff.kotea.core.CommandsFlowHandler
import javax.inject.Inject

class QuizCommandFlowHandler @Inject constructor(
    private val quizRepository: QuizRepository,
    private val prefRepository: PreferencesRepository,
) : CommandsFlowHandler<QuizCommand, CommandEvent> {

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun handle(commands: Flow<QuizCommand>): Flow<CommandEvent> =
        commands.flatMapMerge {
            when (it) {
                is QuizCommand.LoadQuestions -> handleLoading(it.type)
                is QuizCommand.SendAnswers -> handleAnswers(it)
                QuizCommand.MarkMainQuizFilled -> handleMarkingAsFilled()
            }
        }

    private fun handleLoading(type: QuizType): Flow<CommandEvent> = flow {
        val event = quizRepository.getQuestions(type)
            .map { CommandEvent.QuestionsLoaded(it, type) }
            .getOrDefault(CommandEvent.GeneralFail)
        emit(event)
    }

    private fun handleAnswers(command: QuizCommand.SendAnswers): Flow<CommandEvent> = flow {
        val event = quizRepository.saveAnswers(command.type, command.answers)
            .map { CommandEvent.AnswersSent(command.type) }
            .getOrDefault(CommandEvent.GeneralFail)
        emit(event)
    }

    private fun handleMarkingAsFilled(): Flow<CommandEvent> = flow {
        prefRepository.setHasCompletedMainQuiz(true)
    }
}