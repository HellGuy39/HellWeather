package com.hellguy39.hellweather.domain.models

import java.io.Serializable

data class CurrentWeather(
    var name: String = "N/A",

    var dt: Long = 0,
    var temp: String = "N/A",
    var tempFeelsLike: String = "N/A",

    var humidity: String = "N/A",
    var pressure: String = "N/A",
    var uvi: Double = 1.0,
    var dewPoint: String = "N/A",
    var visibility: Int = 0,

    var sunrise: Long = 0,
    var sunset: Long = 0,

    var windSpeed: String = "N/A",
    var windDeg: String = "N/A",
    var windGust: Double = 0.0,

    var wMain: String = "N/A",
    var wDescription: String = "N/A",
    var icon: String = "N/A",

    var tempMax: String = "N/A",
    var tempMin: String = "N/A",

    var requestResult: String = "N/A",
    var errorBody: String = "N/A"
) : Serializable
