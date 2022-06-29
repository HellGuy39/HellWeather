package com.hellguy39.hellweather.domain.model

data class OneCallWeather(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezoneOffset: Int?,
    val currentWeather: CurrentWeather?
)

data class  CurrentWeather(
    val date: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val temp: Double?,
    val feelsLike: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val dewPoint: Double?,
    val uvi: Double?,
    val clouds: Int?,
    val visibility: Int?,
    val windSpeed: Double?,
    val windDeg: Int?,
    val windGust: Double?
)