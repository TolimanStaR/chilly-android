package com.chilly.android.presentation.screens.profile

import com.chilly.android.R
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class ProfileNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<ProfileNews> {

    override suspend fun emit(value: ProfileNews) {
        when (value) {
            ProfileNews.GeneralFail -> snackbarShower.show(R.string.general_fail_message)
            ProfileNews.NavigateSignIn -> router.newRootScreen(Destination.LogIn)
            ProfileNews.NavigateBack -> router.exit()
            ProfileNews.NavigateOnboarding -> router.navigateTo(Destination.Onboarding())
            ProfileNews.InterestsCleared -> snackbarShower.show(R.string.interests_cleared_snackbar)
        }
    }

}