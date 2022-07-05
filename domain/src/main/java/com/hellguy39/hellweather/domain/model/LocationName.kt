package com.hellguy39.hellweather.domain.model

data class LocationInfo(
    val lat: Double?,
    val lon: Double?,
    val name: String?,
    val localNames: Map<String, String>?,
    val country: String?,
    val state: String?
)
