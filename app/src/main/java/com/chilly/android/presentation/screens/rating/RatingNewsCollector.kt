package com.chilly.android.presentation.screens.rating

import com.chilly.android.R
import com.chilly.android.presentation.common.structure.SnackbarShower
import kotlinx.coroutines.flow.FlowCollector
import javax.inject.Inject

class RatingNewsCollector @Inject constructor(
    private val snackbarShower: SnackbarShower
) : FlowCollector<RatingNews> {

    override suspend fun emit(value: RatingNews) {
        when (value) {
            RatingNews.GeneralFail -> snackbarShower.show(R.string.general_fail_message)
            RatingNews.SentSuccess -> snackbarShower.show(R.string.comment_sent_message)
        }
    }
}