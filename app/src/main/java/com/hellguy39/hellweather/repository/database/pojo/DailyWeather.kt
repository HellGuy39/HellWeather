package com.hellguy39.hellweather.repository.database.pojo

data class DailyWeather(
    var temp: String = "N/A",
    var dt: String = "N/A",
    var humidity: String = "N/A",
    var icon: String = "N/A",
    var pop: String = "N/A",

    var min: String = "N/A",
    var max: String = "N/A",
    var day: String = "N/A",
    var night: String = "N/A",
    var eve: String = "N/A",
    var morn: String = "N/A",
)
