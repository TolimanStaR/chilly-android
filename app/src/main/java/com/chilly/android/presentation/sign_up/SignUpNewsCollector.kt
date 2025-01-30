package com.chilly.android.presentation.sign_up

import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import com.chilly.android.R
import com.chilly.android.di.screens.SignUpScope
import com.chilly.android.presentation.common.structure.ResourcesHolder
import com.chilly.android.presentation.common.structure.getString
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import jakarta.inject.Inject
import kotlinx.coroutines.flow.FlowCollector

@SignUpScope
class SignUpNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarHostState: SnackbarHostState,
    private val resourcesHolder: ResourcesHolder
) : FlowCollector<SignUpNews> {

    override suspend fun emit(value: SignUpNews) {
        when(value) {
            SignUpNews.NavigateMain -> router.navigateTo(Destination.Main)
            SignUpNews.NavigateToLogin -> router.backTo(Destination.LogIn)
            is SignUpNews.ShowFailedSnackbar -> snackbarHostState.showSnackbar(
                resourcesHolder.getString(value.reason.toMessageRes())
            )
        }
    }

    @StringRes
    private fun FailReason.toMessageRes(): Int = when(this) {
        FailReason.DataConflict -> R.string.username_taken_error
        FailReason.GeneralFail -> R.string.sign_up_fail_default
    }
}
