package com.chilly.android.presentation.screens.history

import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class HistoryNewsCollector @Inject constructor(
    private val router: Router
) : FlowCollector<HistoryNews> {

    override suspend fun emit(value: HistoryNews) {
        when (value) {
            is HistoryNews.NavigatePlaceInfo -> router.navigateTo(Destination.PlaceInfo(value.place.id, value.place.name))
        }
    }
}