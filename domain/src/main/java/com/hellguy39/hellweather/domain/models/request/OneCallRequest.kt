package com.hellguy39.hellweather.domain.models.request

data class OneCallRequest(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var exclude: String = "minutely,alerts",
    var units: String = "N/A",
    var lang: String = "en",
    var appId: String = "N/A"
)
