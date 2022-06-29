package com.hellguy39.hellweather.data.remote.dto

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

//@JsonClass(generateAdapter = true)
data class OneCallWeatherDto(
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?,
    @field:Json(name = "timezone") val timezone: String?,
    @field:Json(name = "timezone_offset") val timezoneOffset: Int?,
    @field:Json(name = "current") val currentWeather: CurrentWeatherDto?

)

@Json(name = "current")
data class CurrentWeatherDto(
    @field:Json(name = "dt") val date: Long?,
    @field:Json(name = "sunrise") val sunrise: Long?,
    @field:Json(name = "sunset") val sunset: Long?,
    @field:Json(name = "temp") val temp: Double?,
    @field:Json(name = "feels_like") val feelsLike: Double?,
    @field:Json(name = "pressure") val pressure: Int?,
    @field:Json(name = "humidity") val humidity: Int?,
    @field:Json(name = "dew_point") val dewPoint: Double?,
    @field:Json(name = "uvi") val uvi: Double?,
    @field:Json(name = "clouds") val clouds: Int?,
    @field:Json(name = "visibility") val visibility: Int?,
    @field:Json(name = "wind_speed") val windSpeed: Double?,
    @field:Json(name = "wind_deg") val windDeg: Int?,
    @field:Json(name = "wind_gust") val windGust: Double?
)