package com.hellguy39.hellweather.data.repository

import com.hellguy39.hellweather.data.json.LocationInfoParser
import com.hellguy39.hellweather.data.json.OneCallWeatherParser
import com.hellguy39.hellweather.data.local.WeatherDao
import com.hellguy39.hellweather.data.local.WeatherForecastEntity
import com.hellguy39.hellweather.data.mapper.toLocationInfo
import com.hellguy39.hellweather.data.mapper.toLocationInfoDto
import com.hellguy39.hellweather.data.mapper.toOneCallWeather
import com.hellguy39.hellweather.data.mapper.toOneCallWeatherDto
import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.domain.repository.LocalRepository

class LocalRepositoryImpl(
    private val dao: WeatherDao,
    private val oneCallWeatherParser: OneCallWeatherParser,
    private val locationInfoParser: LocationInfoParser
) : LocalRepository {

    override suspend fun insertWeatherForecast(weatherForecast: WeatherForecast) {
        dao.insertWeather(
            WeatherForecastEntity(
                weatherJson = oneCallWeatherParser.parseToJson(weatherForecast.oneCallWeather?.toOneCallWeatherDto()),
                locationJson = locationInfoParser.parseToJson(weatherForecast.locationInfo?.map { it.toLocationInfoDto() })
            )
        )
    }

    override suspend fun getWeatherForecast(): WeatherForecast? {
        dao.getWeatherList().let { list ->
            if (list.isNotEmpty()) {
                return WeatherForecast(
                    oneCallWeatherParser.parseFromJson(list[0].weatherJson)?.toOneCallWeather(),
                    locationInfoParser.parseFromJson(list[0].locationJson)?.map { it.toLocationInfo() }
                )
            } else
                return null
        }
    }

    override suspend fun clearWeatherForecast() {
        dao.clearWeather()
    }
}