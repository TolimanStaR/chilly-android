package com.chilly.android.presentation.screens.place

import com.chilly.android.R
import com.chilly.android.presentation.common.structure.SnackbarShower
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class PlaceInfoNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower
) : FlowCollector<PlaceInfoNews> {

    override suspend fun emit(value: PlaceInfoNews) {
        when (value) {
            PlaceInfoNews.GeneralFail -> {
                snackbarShower.show(R.string.general_fail_message)
            }
            PlaceInfoNews.NavigateUp -> {
                router.exit()
            }
        }
    }
}