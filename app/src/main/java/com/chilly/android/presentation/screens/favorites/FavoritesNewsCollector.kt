package com.chilly.android.presentation.screens.favorites

import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import okhttp3.Route
import javax.inject.Inject

class FavoritesNewsCollector @Inject constructor(
    private val router: Router
) : FlowCollector<FavoritesNews> {

    override suspend fun emit(value: FavoritesNews) {
        when (value) {
            is FavoritesNews.NavigateToPlace -> router.navigateTo(Destination.PlaceInfo(value.placeId))
        }
    }
}