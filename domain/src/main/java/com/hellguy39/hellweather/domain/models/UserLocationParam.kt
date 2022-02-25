package com.hellguy39.hellweather.domain.models

import java.io.Serializable

data class UserLocationParam(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var locationName: String = "N/A",
    var country: String = "N/A",
    var cod: String = "N/A",
    var timezone: Int = 0,
    var id: Int = 0
) : Serializable
