package com.chilly.android.presentation.screens.main


import com.chilly.android.R
import com.chilly.android.data.remote.dto.QuizType
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class MainNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<MainNews> {

     override suspend fun emit(value: MainNews) {
        when(value) {
            MainNews.NavigateLogin -> router.newRootScreen(Destination.LogIn)
            MainNews.NavigateOnboarding -> router.navigateTo(Destination.Onboarding())
            MainNews.NavigateMainQuiz -> router.navigateTo(Destination.Quiz(QuizType.BASE))
            MainNews.NavigateShortQuiz -> router.navigateTo(Destination.Quiz(QuizType.SHORT))
            MainNews.GeneralFail -> snackbarShower.show(R.string.general_fail_message)
            is MainNews.NavigatePlace -> router.navigateTo(Destination.PlaceInfo(value.placeId))
        }
    }
}