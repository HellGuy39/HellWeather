package com.hellguy39.hellweather.data.remote.dto

import com.squareup.moshi.Json

data class OneCallWeatherDto(
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?,
    @field:Json(name = "timezone") val timezone: String?,
    @field:Json(name = "timezone_offset") val timezoneOffset: Int?,
    @field:Json(name = "current") val currentWeather: CurrentWeatherDto?,
    @field:Json(name = "daily") val dailyWeather: List<DailyWeatherDto>?,
    @field:Json(name = "hourly") val hourlyWeather: List<HourlyWeatherDto>?,
    @field:Json(name = "alerts") val alerts: List<AlertDto>?
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
    @field:Json(name = "wind_gust") val windGust: Double?,
    @field:Json(name = "weather") val weather: List<WeatherDto>?
)

data class DailyWeatherDto(
    @field:Json(name = "dt") val date: Long?,
    @field:Json(name = "sunrise") val sunrise: Long?,
    @field:Json(name = "sunset") val sunset: Long?,
    @field:Json(name = "moonrise") val moonrise: Long?,
    @field:Json(name = "moonset") val moonSet: Long?,
    @field:Json(name = "moon_phase") val moonPhase: Double?,
    @field:Json(name = "pressure") val pressure: Int?,
    @field:Json(name = "humidity") val humidity: Int?,
    @field:Json(name = "dew_point") val dewPoint: Double?,
    @field:Json(name = "wind_speed") val windSpeed: Double?,
    @field:Json(name = "wind_deg") val windDeg: Int?,
    @field:Json(name = "wind_gust") val windGust: Double?,
    @field:Json(name = "uvi") val uvi: Double?,
    @field:Json(name = "rain") val rain: Double?,
    @field:Json(name = "pop") val pop: Double?,
    @field:Json(name = "clouds") val clouds: Int?,
    @field:Json(name = "temp") val temp: TempDto?,
    @field:Json(name = "feels_like") val feelsLike: FeelsLikeDto?,
    @field:Json(name = "weather") val weather: List<WeatherDto>?
)

@Json(name = "temp")
data class TempDto(
    @field:Json(name = "day") val day: Double?,
    @field:Json(name = "min") val min: Double?,
    @field:Json(name = "max") val max: Double?,
    @field:Json(name = "night") val night: Double?,
    @field:Json(name = "eve") val eve: Double?,
    @field:Json(name = "morn") val morn: Double?,
)

@Json(name = "feels_like")
data class FeelsLikeDto(
    @field:Json(name = "day") val day: Double?,
    @field:Json(name = "night") val night: Double?,
    @field:Json(name = "eve") val eve: Double?,
    @field:Json(name = "morn") val morn: Double?,
)

data class HourlyWeatherDto(
    @field:Json(name = "dt") val date: Long?,
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
    @field:Json(name = "wind_gust") val windGust: Double?,
    @field:Json(name = "weather") val weather: List<WeatherDto>?,
    @field:Json(name = "pop") val pop: Double?,
)

data class WeatherDto(
    @field:Json(name = "id") val id: Int?,
    @field:Json(name = "main") val main: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "icon") val icon: String?,
)

data class AlertDto(
    @field:Json(name = "sender_name") val senderName: String?,
    @field:Json(name = "event") val event: String?,
    @field:Json(name = "description") val description: String?,
    @field:Json(name = "start") val start: Long?,
    @field:Json(name = "end") val end: Long?,
    @field:Json(name = "tags") val tags: List<String>?,
)
