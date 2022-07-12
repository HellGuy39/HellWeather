package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetCachedForecastUseCase(
    private val localRepo: LocalRepository
) {
    suspend operator fun invoke(): Flow<Resource<WeatherForecast>> {
        return flow{
            val cachedData = localRepo.getWeatherForecast()

            if (cachedData != null)
                emit(Resource.Success(data = cachedData))
            else
                emit(Resource.Error(message = "No cached data"))

            return@flow
        }
    }
}