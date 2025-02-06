package com.chilly.android.presentation.login

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.R
import com.chilly.android.di.screens.LoginScope
import com.chilly.android.presentation.common.structure.ResourcesHolder
import com.chilly.android.presentation.common.structure.getString
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

@LoginScope
class LoginNewsCollector @Inject constructor(
    private val router: Router,
    private val snackBarHostState: SnackbarHostState,
    private val resourcesHolder: ResourcesHolder
) : FlowCollector<LoginNews> {

    override suspend fun emit(value: LoginNews) {
        when(value) {
            LoginNews.NavigateMain -> router.newRootScreen(Destination.Main)
            LoginNews.LoginFailed -> snackBarHostState.showSnackbar(getString(R.string.login_failed_snackbar))
            LoginNews.NavigateSignUp -> router.navigateTo(Destination.SignUp)
            LoginNews.NavigateForgotPassword -> router.navigateTo(Destination.ForgotPassword)
        }
    }

    private fun getString(@StringRes id: Int): String = resourcesHolder.getString(id)
}