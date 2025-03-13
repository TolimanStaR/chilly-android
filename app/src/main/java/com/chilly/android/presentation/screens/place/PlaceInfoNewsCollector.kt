package com.chilly.android.presentation.screens.place

import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class PlaceInfoNewsCollector @Inject constructor(
) : FlowCollector<PlaceInfoNews> {

    override suspend fun emit(value: PlaceInfoNews) {
        when (value) {
            else -> Unit
        }
    }
}