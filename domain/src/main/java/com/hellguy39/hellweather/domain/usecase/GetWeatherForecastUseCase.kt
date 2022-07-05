package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.async
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
        fetchFromRemote: Boolean
    ): Flow<Resource<WeatherForecast>> {
        return flow {
            coroutineScope {
                emit(Resource.Loading(true))

                if (fetchFromRemote) {
                    val oneCallWeatherTask = async {
                        remoteRepo.getOneCallWeather(lat, lon)
                    }
                    val locationTask = async {
                        remoteRepo.getLocationInfo(lat, lon, 5)
                    }

                    val oneCallWeather = oneCallWeatherTask.await()
                    val location = locationTask.await()

                    if (oneCallWeather == null || location == null)
                        emit(Resource.Error("Couldn't load data"))
                    else
                        emit(Resource.Success(WeatherForecast(oneCallWeather, location)))
                }

                emit(Resource.Loading(false))
            }
        }
    }
}