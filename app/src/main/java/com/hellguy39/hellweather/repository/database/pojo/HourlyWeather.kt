package com.hellguy39.hellweather.repository.database.pojo

data class HourlyWeather(
    var dt: String = "N/A",
    var temp: String = "N/A",
    var tempFeelsLike: String = "N/A",
    var pressure: String = "N/A",
    var humidity: String = "N/A",
    var pop: Double = 0.0,
    var windSpeed: String = "N/A"
)
