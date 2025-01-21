package com.chilly.android.presentation.login

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.R
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.FlowCollector

class LoginNewsCollector @AssistedInject constructor(
    private val router: Router,
    @Assisted private val snackBarHostState: SnackbarHostState,
    @Assisted private val resources: Resources
) : FlowCollector<LoginNews> {

    override suspend fun emit(value: LoginNews) {
        when(value) {
            LoginNews.NavigateMain -> router.newRootScreen(Destination.Main)
            LoginNews.LoginFailed -> snackBarHostState.showSnackbar(resources.getString(R.string.login_failed_snackbar))
            LoginNews.NavigateSignUp -> router.navigateTo(Destination.Main)
        }
    }

    @AssistedFactory
    interface Factory {
        fun build(snackBarHostState: SnackbarHostState, resources: Resources): LoginNewsCollector
    }
}