package com.chilly.android.presentation.screens.profile

import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class ProfileNewsCollector @Inject constructor(
) : FlowCollector<ProfileNews> {

    override suspend fun emit(value: ProfileNews) {
        when (value) {
            else -> Unit
        }
    }
}