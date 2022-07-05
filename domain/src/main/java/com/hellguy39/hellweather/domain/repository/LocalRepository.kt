package com.hellguy39.hellweather.domain.repository

import com.hellguy39.hellweather.domain.model.WeatherForecast

interface LocalRepository {

    suspend fun insertWeatherForecast(weatherForecast: WeatherForecast)

    suspend fun getWeatherForecast() : WeatherForecast?

    suspend fun clearWeatherForecast()

}