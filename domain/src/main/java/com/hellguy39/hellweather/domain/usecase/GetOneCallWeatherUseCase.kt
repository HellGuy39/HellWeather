package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.repository.OpenWeatherRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetOneCallWeatherUseCase(
    private val repository: OpenWeatherRepository
) {
    suspend operator fun invoke(
        fetchFromRemote: Boolean,
        lat: Double,
        lon: Double
    ): Flow<Resource<OneCallWeather>> {
        return repository.getOneCallWeather(fetchFromRemote = fetchFromRemote, lat = lat, lon = lon)
    }
}