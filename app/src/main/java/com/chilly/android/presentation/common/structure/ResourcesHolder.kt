package com.chilly.android.presentation.common.structure

import android.content.Context
import androidx.activity.ComponentActivity
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import androidx.work.WorkManager
import com.chilly.android.di.activity.ActivityScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

@ActivityScope
class SnackbarShower @Inject constructor(
    private val activity: ComponentActivity,
    private val snackbarHostState: SnackbarHostState
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun show(@StringRes resId: Int) = show(activity.getString(resId))

    fun show(text: String) {
        scope.launch {
            runCatching {
                snackbarHostState.showSnackbar(text)
            }.onFailure {
                Timber.e("got error during showing of snackBar", it)
            }
        }
    }
}

@Singleton
class WorkManagerProvider @Inject constructor(
    private val applicationContext: Context
) {

    fun getInstance(): WorkManager = WorkManager.getInstance(applicationContext)
}