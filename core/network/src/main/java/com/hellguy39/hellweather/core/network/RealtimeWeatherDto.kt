package com.hellguy39.hellweather.core.network

import com.hellguy39.hellweather.core.network.dto.CurrentWeatherDto
import com.hellguy39.hellweather.core.network.dto.LocationDto
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class RealtimeWeatherDto(
    @SerialName("location")
    val locationDto: LocationDto?,
    @SerialName("current")
    val currentWeatherDto: CurrentWeatherDto?
)