package com.chilly.android.presentation.forgot_password

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.R
import com.chilly.android.presentation.common.structure.ResourcesHolder
import com.chilly.android.presentation.common.structure.getString
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class ForgotPasswordNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarHostState: SnackbarHostState,
    private val resourcesHolder: ResourcesHolder
) : FlowCollector<ForgotPasswordNews> {

    override suspend fun emit(value: ForgotPasswordNews) {
        when(value) {
            ForgotPasswordNews.NavigateLogin -> router.backTo(Destination.LogIn)
            ForgotPasswordNews.NavigateSignUp -> router.replaceScreen(Destination.SignUp)
            ForgotPasswordNews.FailedRequest -> showSnackbar(R.string.genera_fail_message)
            ForgotPasswordNews.PasswordChanged -> showSnackbar(R.string.password_changed)
            ForgotPasswordNews.WrongCode -> showSnackbar(R.string.wrong_code)
            ForgotPasswordNews.WrongEmail -> showSnackbar(R.string.wrong_code)
        }
    }

    private suspend fun showSnackbar(@StringRes resId: Int) = snackbarHostState.showSnackbar(getString(resId))

    private fun getString(@StringRes resId: Int): String = resourcesHolder.getString(resId)
}