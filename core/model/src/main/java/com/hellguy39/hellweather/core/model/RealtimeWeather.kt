package com.hellguy39.hellweather.core.model

data class RealtimeWeather(
    val currentWeather: CurrentWeather?,
    val location: Location?
)