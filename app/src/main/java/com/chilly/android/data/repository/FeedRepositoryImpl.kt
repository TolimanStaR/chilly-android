package com.chilly.android.data.repository

import com.chilly.android.data.remote.api.FeedApi
import com.chilly.android.data.remote.dto.PlaceDto
import com.chilly.android.domain.repository.FeedRepository
import com.chilly.android.domain.repository.LocationRepository
import com.chilly.android.domain.repository.PlaceRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import timber.log.Timber
import kotlin.math.hypot

class FeedRepositoryImpl(
    private val feedApi: FeedApi,
    private val placeRepository: PlaceRepository,
    private val locationRepository: LocationRepository
) : FeedRepository {

    private var locationForFeed: LocationRepository.LocationData? = null
    private var lastPage: Int = -1
    private var wasLastPageEmpty: Boolean = false
    private val currentFeedFlow: MutableStateFlow<List<PlaceDto>> = MutableStateFlow(emptyList())

    override fun getFeedFlow(): Flow<List<PlaceDto>> = currentFeedFlow

    override suspend fun requestNextPage(): Result<Unit> {
        if (locationForFeed == null) {
            updateLocation()
        }

        lastPage++
        return fetchFeedPage(currentLocation())
    }

    override suspend fun refreshFeed(): Result<Boolean> {
        val prevLocation = locationForFeed
        updateLocation()
        val currentLocation = currentLocation()
        lastPage = 0
        wasLastPageEmpty = false

        return fetchFeedPage(currentLocation, clear = true)
            .map { currentLocation.differ(prevLocation, 0.001) }
    }

    private fun currentLocation() = locationForFeed ?: LocationRepository.LocationData(0.0, 0.0)

    private suspend fun updateLocation() {
        if (locationForFeed != null) return
        locationRepository.getCurrentLocation()
            .onSuccess {
                locationForFeed = it
                Timber.i("location updated: $it")
            }
            .onFailure {
                Timber.e(it)
            }
    }

    private suspend fun fetchFeedPage(location: LocationRepository.LocationData, clear: Boolean = false): Result<Unit> {
        if (wasLastPageEmpty) {
            return Result.success(Unit)
        }
        return feedApi.getFeedPage(location, lastPage)
            .map { newPage ->
                if (newPage.isEmpty()) {
                    wasLastPageEmpty = true
                }
                Timber.i("new feed page($lastPage): $newPage")

                currentFeedFlow.update { previous ->
                    if (!clear) {
                        previous.toMutableList().apply {

                            addAll(newPage)
                        }
                    } else {
                        newPage
                    }
                }

                placeRepository.savePlaces(newPage, writeToHistory = false)
            }
    }

    private fun LocationRepository.LocationData.differ(other: LocationRepository.LocationData?, threshold: Double): Boolean {
        other ?: return true
        return hypot(latitude - other.latitude, longitude - other.longitude) >= threshold
    }
}