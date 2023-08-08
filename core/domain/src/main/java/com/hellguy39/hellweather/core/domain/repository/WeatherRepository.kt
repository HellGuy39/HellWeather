package com.hellguy39.hellweather.core.domain.repository

import com.hellguy39.hellweather.core.model.RealtimeWeather
import com.hellguy39.hellweather.core.model.RequestQuery
import com.hellguy39.hellweather.core.model.Resource
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {

    fun getRealtimeWeather(requestQuery: RequestQuery): Flow<Resource<RealtimeWeather>>

}