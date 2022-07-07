package com.hellguy39.hellweather.domain.usecase

import android.util.Log
import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetWeatherForecastUseCase(
    private val remoteRepo: RemoteRepository,
    private val localRepo: LocalRepository
) {
    suspend operator fun invoke(
        lat: Double,
        lon: Double,
        fetchFromRemote: Boolean
    ): Flow<Resource<WeatherForecast>> {
        return flow {
            coroutineScope {
                emit(Resource.Loading(true))

                localRepo.getWeatherForecast()?.let { cachedData ->
                    emit(Resource.Success(cachedData))
                }

                if (fetchFromRemote) {
                    val oneCallWeatherTask = async {
                        remoteRepo.getOneCallWeather(lat, lon)
                    }
                    val locationTask = async {
                        remoteRepo.getLocationInfo(lat, lon, 5)
                    }

                    val oneCallWeather = oneCallWeatherTask.await()
                    val location = locationTask.await()

                    if (oneCallWeather != null || location != null) {
                        localRepo.insertWeatherForecast(WeatherForecast(oneCallWeather, location))
                        emit(Resource.Success(WeatherForecast(oneCallWeather, location)))
                    } else
                        emit(Resource.Error("Couldn't load data"))
                }

                emit(Resource.Loading(false))
            }
        }
    }
}