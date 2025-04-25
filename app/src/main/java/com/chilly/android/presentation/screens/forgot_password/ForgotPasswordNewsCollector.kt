package com.chilly.android.presentation.screens.forgot_password

import com.chilly.android.R
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class ForgotPasswordNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<ForgotPasswordNews> {

    override suspend fun emit(value: ForgotPasswordNews) {
        when(value) {
            ForgotPasswordNews.NavigateLogin -> router.backTo(Destination.LogIn)
            ForgotPasswordNews.NavigateSignUp -> router.replaceScreen(Destination.SignUp)
            ForgotPasswordNews.FailedRequest -> snackbarShower.show(R.string.general_fail_message)
            ForgotPasswordNews.PasswordChanged -> snackbarShower.show(R.string.password_changed)
            ForgotPasswordNews.WrongCode -> snackbarShower.show(R.string.wrong_code)
            ForgotPasswordNews.WrongEmail -> snackbarShower.show(R.string.wrong_code)
        }
    }

}