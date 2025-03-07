package com.chilly.android.presentation.screens.quiz

import com.chilly.android.R
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class QuizNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<QuizNews> {

    override suspend fun emit(value: QuizNews) {
        when (value) {
            QuizNews.GeneralFail -> snackbarShower.show(R.string.general_fail_message)
            QuizNews.NavigateBack -> router.exit()
            QuizNews.NavigateToRecommendationResult -> router.replaceScreen(Destination.RecommendationResult)
            QuizNews.NavigateShortQuiz -> router.replaceScreen(Destination.Quiz(QuizType.SHORT))
        }
    }
}