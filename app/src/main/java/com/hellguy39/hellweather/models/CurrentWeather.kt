package com.hellguy39.hellweather.models

data class CurrentWeather(
    var dt: String = "N/A",
    var temp: String = "N/A",
    var tempFeelsLike: String = "N/A",
    var humidity: String = "N/A",
    var pressure: String = "N/A",
    var sunrise: String = "N/A",
    var sunset: String = "N/A",
    var windSpeed: String = "N/A",
    var wMain: String = "N/A",
    var wDescription: String = "N/A",
    var icon: String = "N/A"
)
