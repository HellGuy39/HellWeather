package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.LocationName
import com.hellguy39.hellweather.domain.repository.OpenWeatherRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow

class GetLocationNameUseCase(
    private val repository: OpenWeatherRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        limit: Int
    ) : Flow<Resource<List<LocationName>>> {
        return repository.getLocationName(lat, lon, limit)
    }
}