package com.hellguy39.hellweather.domain.model

data class WeatherForecast(
    val oneCallWeather: OneCallWeather?,
    val locationInfo: List<LocationInfo>?
)
