package com.hellguy39.hellweather.domain.models.weather

import java.io.Serializable

data class HourlyWeather(
    var dt: Long = 0,
    var temp: String = "N/A",
    var tempFeelsLike: String = "N/A",
    var pressure: String = "N/A",
    var humidity: String = "N/A",
    var pop: Double = 0.0,
    var windSpeed: String = "N/A",
    var icon: String = "N/A"
) : Serializable
