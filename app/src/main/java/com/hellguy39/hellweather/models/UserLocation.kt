package com.hellguy39.hellweather.models

data class UserLocation(
    var lat: String = "N/A",
    var lon: String = "N/A",
    var cityName: String = "N/A",
    var region: String = "N/A",
    var cod: String = "N/A",
    var id: Int = 0,
    var timezone: String = "N/A"
)
