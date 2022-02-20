package com.hellguy39.hellweather.domain.models

import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY

data class OneCallRequest(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var exclude: String = "minutely,alerts",
    var units: String = METRIC,
    var lang: String = "en",
    var appId: String = OPEN_WEATHER_API_KEY
)
