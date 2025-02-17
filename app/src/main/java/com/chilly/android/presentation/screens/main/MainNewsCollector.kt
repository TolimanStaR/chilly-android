package com.chilly.android.presentation.screens.main


import com.chilly.android.presentation.navigation.Destination
import com.chilly.android.presentation.screens.quiz.QuizType
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class MainNewsCollector @Inject constructor(
    private val router: Router
) : FlowCollector<MainNews> {

     override suspend fun emit(value: MainNews) {
        when(value) {
            MainNews.NavigateLogin -> router.newRootScreen(Destination.LogIn)
            MainNews.NavigateOnboarding -> router.navigateTo(Destination.Onboarding())
            MainNews.NavigateMainQuiz -> router.navigateTo(Destination.Quiz(QuizType.MAIN))
            MainNews.NavigateShortQuiz -> router.navigateTo(Destination.Quiz(QuizType.SHORT))
        }
    }
}