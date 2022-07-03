package com.hellguy39.hellweather.data.remote.dto

import com.squareup.moshi.Json

data class LocationNameDto(
    @field:Json(name = "lat") val lat: Double?,
    @field:Json(name = "lon") val lon: Double?,
    @field:Json(name = "name") val name: String?,
    @field:Json(name = "local_names") val localNames: Map<String, String>?,
    @field:Json(name = "country") val country: String?,
    @field:Json(name = "state") val state: String?
)
