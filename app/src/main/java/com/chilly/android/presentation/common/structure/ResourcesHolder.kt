package com.chilly.android.presentation.common.structure

import android.content.res.Resources
import androidx.annotation.StringRes
import androidx.compose.material3.SnackbarHostState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ResourcesHolder @Inject constructor() {
    private var _resources: Resources? = null

    fun set(resources: Resources) {
        _resources = resources
    }

    fun release() {
        _resources = null
    }

    fun get() = _resources ?: throw IllegalStateException()
}

fun ResourcesHolder.getString(@StringRes id: Int) = get().getString(id)

class SnackbarShower @Inject constructor(
    private val resourcesHolder: ResourcesHolder,
    private val snackbarHostState: SnackbarHostState
) {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    fun show(@StringRes resId: Int) = show(resourcesHolder.getString(resId))

    fun show(text: String) {
        scope.launch {
            snackbarHostState.showSnackbar(text)
        }
    }
}