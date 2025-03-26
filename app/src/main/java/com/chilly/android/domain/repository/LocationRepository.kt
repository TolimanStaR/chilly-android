package com.chilly.android.domain.repository

interface LocationRepository {

    suspend fun getCurrentLocation(): Result<LocationData>

    class NoPermissionException : Exception()
    class NoLocationProviderException : Exception()

    data class LocationData(
        val latitude: Double,
        val longitude: Double
    )
}
