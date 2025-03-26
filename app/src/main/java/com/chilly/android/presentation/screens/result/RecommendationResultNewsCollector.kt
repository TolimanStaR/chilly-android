package com.chilly.android.presentation.screens.result

import com.chilly.android.presentation.common.structure.SnackbarShower
import com.chilly.android.presentation.common.structure.WorkManagerProvider
import com.chilly.android.presentation.navigation.Destination
import com.github.terrakok.cicerone.Router
import kotlinx.coroutines.flow.FlowCollector
import timber.log.Timber
import javax.inject.Inject

class RecommendationResultNewsCollector @Inject constructor(
    private val router: Router,
    private val snackbarShower: SnackbarShower,
    private val workManagerProvider: WorkManagerProvider
) : FlowCollector<RecommendationResultNews> {

    override suspend fun emit(value: RecommendationResultNews) {
        when (value) {
            RecommendationResultNews.GeneralFail -> snackbarShower.show("fail")
            is RecommendationResultNews.NavigatePlace -> router.navigateTo(Destination.PlaceInfo(value.id))
            is RecommendationResultNews.SubmitNotificationRequest -> workManagerProvider.getInstance()
                .run {
                    Timber.i("Notification Work enqueued")
                    cancelAllWorkByTag(NotificationWork.TAG)
                    enqueue(value.request)
                }
        }
    }
}