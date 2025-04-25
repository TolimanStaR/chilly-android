package com.chilly.android.presentation.screens.login

import com.chilly.android.R
import com.chilly.android.di.screens.LoginScope
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

@LoginScope
class LoginNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<LoginNews> {

    override suspend fun emit(value: LoginNews) {
        when(value) {
            LoginNews.NavigateMain -> router.newRootScreen(Destination.Main)
            LoginNews.LoginFailed -> snackbarShower.show(R.string.login_failed_snackbar)
            LoginNews.NavigateSignUp -> router.navigateTo(Destination.SignUp)
            LoginNews.NavigateForgotPassword -> router.navigateTo(Destination.ForgotPassword)
        }
    }
}