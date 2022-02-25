package com.hellguy39.hellweather.domain.request_models

data class CurrentByCoordinatesRequest(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var units: String = "N/A",
    var lang: String = "N/A",
    var appId: String = "N/A"
)
