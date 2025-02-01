package com.chilly.android.presentation.common.structure

import android.content.res.Resources
import androidx.annotation.StringRes
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