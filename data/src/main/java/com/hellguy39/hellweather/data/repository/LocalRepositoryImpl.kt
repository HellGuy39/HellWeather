package com.hellguy39.hellweather.data.repository

import com.hellguy39.hellweather.data.json.LocationInfoParser
import com.hellguy39.hellweather.data.json.OneCallWeatherParser
import com.hellguy39.hellweather.data.local.WeatherDao
import com.hellguy39.hellweather.data.mapper.toLocationInfo
import com.hellguy39.hellweather.data.mapper.toOneCallWeather
import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val dao: WeatherDao,
    private val oneCallWeatherParser: OneCallWeatherParser,
    private val locationNameParser: LocationInfoParser
) : LocalRepository {
    override suspend fun insertWeatherForecast(weatherForecast: WeatherForecast) {
        //dao.insertWeather()
    }

    override suspend fun getWeatherForecast(): WeatherForecast? {
        dao.getWeatherList().let { list ->
            if (list.isNotEmpty()) {
                return WeatherForecast(
                    oneCallWeatherParser.parse(list[0].weatherJson)?.toOneCallWeather(),
                    locationNameParser.parse(list[0].locationJson)?.map { it.toLocationInfo() }
                )
            } else
                return null

        }
    }

    override suspend fun clearWeatherForecast() {
        dao.clearWeather()
    }
}