package com.hellguy39.hellweather.domain.repository

import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.domain.model.OneCallWeather

interface RemoteRepository {

    suspend fun getOneCallWeather(
        lat: Double,
        lon: Double
    ): OneCallWeather?

    suspend fun getLocationInfo(
        lat: Double,
        lon: Double,
        limit: Int,
    ): List<LocationInfo>?

}