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
            PlaceInfoNews.GeneralFail -> snackbarShower.show(R.string.general_fail_message)
            PlaceInfoNews.NavigateUp -> router.exit()
            PlaceInfoNews.RatingSent -> snackbarShower.show(R.string.rating_sent_text)
            PlaceInfoNews.EmptyCommentsLoaded -> snackbarShower.show(R.string.empty_comments_received)
            PlaceInfoNews.CommentModified -> snackbarShower.show(R.string.comment_modified_message)
        }
    }
}