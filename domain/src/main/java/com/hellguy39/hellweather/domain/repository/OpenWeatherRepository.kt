package com.hellguy39.hellweather.domain.repository

import com.hellguy39.hellweather.domain.model.LocationName
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow

interface OpenWeatherRepository {

    suspend fun getOneCallWeather(
        fetchFromRemote: Boolean,
        lat: Double,
        lon: Double
    ): Flow<Resource<OneCallWeather>>

    suspend fun getLocationName(
        lat: Double,
        lon: Double,
        limit: Int,
    ): Flow<Resource<List<LocationName>>>

}