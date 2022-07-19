package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetForecastUseCase(
    private val remoteRepo: RemoteRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Flow<Resource<OneCallWeather>> {
        return flow {
            emit(Resource.Loading(true))

            val data = remoteRepo.getOneCallWeather(lat, lon)
            emit(data)

            emit(Resource.Loading(false))
            return@flow
        }
    }
}