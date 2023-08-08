package com.hellguy39.hellweather.service

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface ILocationClient {
    fun getLocationUpdated(interval: Long): Flow<Location>

    class LocationException(message: String): Exception()
}