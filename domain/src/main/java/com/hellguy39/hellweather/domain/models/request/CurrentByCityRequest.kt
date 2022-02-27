package com.hellguy39.hellweather.domain.models.request

data class CurrentByCityRequest(
    var cityName: String = "N/A",
    var units: String = "N/A",
    var lang: String = "N/A",
    var appId: String = "N/A"
)