package com.chilly.android.presentation.screens.main

import com.chilly.android.data.sample.place1
import com.chilly.android.data.sample.place2
import com.chilly.android.utils.testUpdate
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.BeforeEach

class MainUpdateTest {

    private lateinit var mainUpdate: MainUpdate

    @BeforeEach
    fun setup() {
        mainUpdate = MainUpdate()
    }

    @Test
    fun `when GetRecommendationClicked then CheckMainQuiz command is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(),
            event = MainEvent.UiEvent.GetRecommendationClicked,
            expectedCommandsProducer = { listOf(MainCommand.CheckMainQuiz) }
        )
    }

    @Test
    fun `when LastFeedElementIsVisible then isLoading is true and LoadNewFeedPage command is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(isLoading = false),
            event = MainEvent.UiEvent.LastFeedElementIsVisible,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = { listOf(MainCommand.LoadNewFeedPage) }
        )
    }

    @Test
    fun `when PulledToRefresh then isRefreshing is true and RefreshFeed command is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(isRefreshing = false),
            event = MainEvent.UiEvent.PulledToRefresh,
            expectedStateProducer = { state.copy(isRefreshing = true) },
            expectedCommandsProducer = { listOf(MainCommand.RefreshFeed) }
        )
    }

    @Test
    fun `when ScreenIsShown then isLoading is true and LoadFeed and LoadNewFeedPage commands are emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(isLoading = false),
            event = MainEvent.UiEvent.ScreenIsShown,
            expectedStateProducer = { state.copy(isLoading = true) },
            expectedCommandsProducer = { listOf(MainCommand.LoadFeed, MainCommand.LoadNewFeedPage) }
        )
    }

    @Test
    fun `when PlaceClicked then NavigatePlace news is emitted`() {
        val placeDto = place1

        mainUpdate.testUpdate(
            initialState = MainState(),
            event = MainEvent.UiEvent.PlaceClicked(placeDto),
            expectedNewsProducer = { listOf(MainNews.NavigatePlace(placeDto)) }
        )
    }

    @Test
    fun `when GotPermissionRequestResult with accepted permissions then state is updated accordingly`() {
        val permissions = mapOf(
            "android.permission.ACCESS_FINE_LOCATION" to true,
            "android.permission.ACCESS_COARSE_LOCATION" to false
        )

        mainUpdate.testUpdate(
            initialState = MainState(permissionsChecked = false, locationAccessGranted = false),
            event = MainEvent.UiEvent.GotPermissionRequestResult(permissions),
            expectedStateProducer = { state.copy(permissionsChecked = true, locationAccessGranted = true) }
        )
    }

    @Test
    fun `when GotPermissionRequestResult with denied permissions then PermissionsDenied news is emitted`() {
        val permissions = mapOf(
            "android.permission.ACCESS_FINE_LOCATION" to false,
            "android.permission.ACCESS_COARSE_LOCATION" to false
        )

        mainUpdate.testUpdate(
            initialState = MainState(permissionsChecked = false, locationAccessGranted = false),
            event = MainEvent.UiEvent.GotPermissionRequestResult(permissions),
            expectedStateProducer = { state.copy(permissionsChecked = true, locationAccessGranted = false) },
            expectedNewsProducer = { listOf(MainNews.PermissionsDenied) }
        )
    }

    @Test
    fun `when PermissionsGranted then state is updated accordingly`() {
        mainUpdate.testUpdate(
            initialState = MainState(permissionsChecked = false, locationAccessGranted = false),
            event = MainEvent.UiEvent.PermissionsGranted,
            expectedStateProducer = { state.copy(permissionsChecked = true, locationAccessGranted = true) }
        )
    }

    @Test
    fun `when CheckQuizResult with hasBeenCompleted true then NavigateShortQuiz news is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(),
            event = MainEvent.CommandEvent.CheckQuizResult(hasBeenCompleted = true),
            expectedNewsProducer = { listOf(MainNews.NavigateShortQuiz) }
        )
    }

    @Test
    fun `when CheckQuizResult with hasBeenCompleted false then NavigateMainQuiz news is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(),
            event = MainEvent.CommandEvent.CheckQuizResult(hasBeenCompleted = false),
            expectedNewsProducer = { listOf(MainNews.NavigateMainQuiz) }
        )
    }

    @Test
    fun `when FeedUpdateFailed then isLoading and isRefreshing are set to false and GeneralFail news is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(isLoading = true, isRefreshing = true),
            event = MainEvent.CommandEvent.FeedUpdateFailed,
            expectedStateProducer = { state.copy(isLoading = false, isRefreshing = false) },
            expectedNewsProducer = { listOf(MainNews.GeneralFail) }
        )
    }

    @Test
    fun `when FeedUpdated then state is updated with new feed and loading indicators are reset`() {
        val newFeed = listOf(place1, place2)

        mainUpdate.testUpdate(
            initialState = MainState(isLoading = true, isRefreshing = true),
            event = MainEvent.CommandEvent.FeedUpdated(newFeed),
            expectedStateProducer = { state.copy(feed = newFeed, isLoading = false, isRefreshing = false) }
        )
    }

    @Test
    fun `when SameLocationRefresh then SameLocationWasUsed news is emitted`() {
        mainUpdate.testUpdate(
            initialState = MainState(),
            event = MainEvent.CommandEvent.SameLocationRefresh,
            expectedNewsProducer = { listOf(MainNews.SameLocationWasUsed) }
        )
    }
}
