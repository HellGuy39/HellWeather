package com.hellguy39.hellweather.domain.repository

import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.util.Resource

interface RemoteRepository {

    suspend fun getOneCallWeather(
        lat: Double,
        lon: Double
    ): Resource<OneCallWeather>

    suspend fun getLocationsInfoByCoordinates(
        lat: Double,
        lon: Double,
        limit: Int,
    ): Resource<List<LocationInfo>>

    suspend fun getLocationsInfoByCityName(
        cityName: String,
        limit: Int,
    ): Resource<List<LocationInfo>>

}