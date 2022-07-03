package com.hellguy39.hellweather.domain.model

data class OneCallWeather(
    val lat: Double?,
    val lon: Double?,
    val timezone: String?,
    val timezoneOffset: Int?,
    val currentWeather: CurrentWeather?,
    val dailyWeather: List<DailyWeather>?,
    val hourlyWeather: List<HourlyWeather>?,
    val alerts: List<Alert>?
)

data class CurrentWeather(
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
    val windGust: Double?,
    val weather: List<Weather>?
)

data class DailyWeather(
    val date: Long?,
    val sunrise: Long?,
    val sunset: Long?,
    val moonrise: Long?,
    val moonSet: Long?,
    val moonPhase: Double?,
    val pressure: Int?,
    val humidity: Int?,
    val dewPoint: Double?,
    val windSpeed: Double?,
    val windDeg: Int?,
    val windGust: Double?,
    val uvi: Double?,
    val rain: Double?,
    val pop: Double?,
    val clouds: Int?,
    val temp: Temp?,
    val feelsLike: FeelsLike?,
    val weather: List<Weather>?
)

data class Temp(
    val day: Double?,
    val min: Double?,
    val max: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?,
)

data class FeelsLike(
    val day: Double?,
    val night: Double?,
    val eve: Double?,
    val morn: Double?,
)

data class HourlyWeather(
    val date: Long?,
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
    val windGust: Double?,
    val weather: List<Weather>?,
    val pop: Double?,
)

data class Weather(
    val id: Int?,
    val main: String?,
    val description: String?,
    val icon: String?,
)

data class Alert(
    val senderName: String?,
    val event: String?,
    val description: String?,
    val start: Long?,
    val end: Long?,
    val tags: List<String>?,
)