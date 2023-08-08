package com.hellguy39.hellweather.core.data.repository

import com.hellguy39.hellweather.core.data.mapper.toRealtimeWeather
import com.hellguy39.hellweather.core.domain.repository.WeatherRepository
import com.hellguy39.hellweather.core.model.RealtimeWeather
import com.hellguy39.hellweather.core.model.RequestQuery
import com.hellguy39.hellweather.core.model.Resource
import com.hellguy39.hellweather.core.network.NetworkDataSource
import com.hellguy39.hellweather.core.network.util.RequestResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart

class WeatherRepositoryImpl(
    private val networkDataSource: NetworkDataSource
): WeatherRepository {

    override fun getRealtimeWeather(requestQuery: RequestQuery): Flow<Resource<RealtimeWeather>> {
        return flow {
            val requestResult = networkDataSource.getRealtime(requestQuery)
            val resource = when(requestResult) {
                is RequestResult.Success -> {
                    Resource.Success(requestResult.data.toRealtimeWeather())
                }
                is RequestResult.Error -> {
                    Resource.Error(requestResult.errorMessage)
                }
            }
            emit(resource)
        }
            .onStart { emit(Resource.Loading(true)) }
            .onCompletion { emit(Resource.Loading(false)) }
            .flowOn(Dispatchers.IO)
    }

}