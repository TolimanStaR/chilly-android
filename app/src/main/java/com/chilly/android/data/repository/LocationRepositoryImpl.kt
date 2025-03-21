package com.chilly.android.data.repository

import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.location.LocationManager
import androidx.core.content.ContextCompat
import com.chilly.android.domain.repository.LocationRepository
import com.google.android.gms.location.CurrentLocationRequest
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.tasks.Tasks

class LocationRepositoryImpl(
    private val locationProvider: FusedLocationProviderClient,
    private val context: Context
) : LocationRepository {

    @SuppressLint("MissingPermission")
    override suspend fun getCurrentLocation(): Result<LocationRepository.LocationData> {
        if (!context.checkLocationPermissions()) {
            return Result.failure(LocationRepository.NoPermissionException())
        }

        if (!context.checkLocationProvidersAvailable()) {
            return Result.failure(LocationRepository.NoLocationProviderException())
        }

        val request = CurrentLocationRequest.Builder()
            .setMaxUpdateAgeMillis(120_000L) // 2 minutes
            .build()

        return runCatching {
            Tasks.await(
                locationProvider.getCurrentLocation(request, null)
            )
        }.mapCatching { result ->
            LocationRepository.LocationData(
                latitude = result.latitude,
                longitude = result.longitude
            )
        }
    }

    private fun Context.checkLocationPermissions(): Boolean {
        return ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, ACCESS_COARSE_LOCATION) == PERMISSION_GRANTED
    }

    private fun Context.checkLocationProvidersAvailable(): Boolean {
        val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val gpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val networkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        return gpsEnabled || networkEnabled
    }
}