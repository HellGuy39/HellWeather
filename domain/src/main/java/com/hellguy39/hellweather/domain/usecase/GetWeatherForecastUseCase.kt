package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherForecastUseCase(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
    ): Flow<Resource<WeatherForecast>> {
        return flow {
            coroutineScope {
                emit(Resource.Loading(true))

                val weatherScope = async {
                    remoteRepo.getOneCallWeather(lat, lon)
                }
                val locationScope = async {
                    remoteRepo.getLocationInfo(lat, lon, 5)
                }

                val location = locationScope.await()
                val weatherForecast = weatherScope.await()

                if (location.data != null || weatherForecast.data != null) {
                    localRepo.clearWeatherForecast()
                    localRepo.insertWeatherForecast(WeatherForecast(weatherForecast.data, location.data))
                    emit(Resource.Success(WeatherForecast(weatherForecast.data, location.data)))
                } else
                    emit(Resource.Error("Couldn't load data"))


                emit(Resource.Loading(false))
            }
        }
    }
}