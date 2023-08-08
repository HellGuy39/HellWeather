package com.hellguy39.hellweather.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class LocationDto(

    /* Latitude in decimal degree */
    @SerialName("lat")
    val lat: Double?,

    /* Longitude in decimal degree */
    @SerialName("lon")
    val lon: Double?,

    /* Location name */
    @SerialName("name")
    val name: String?,

    /* Region or state of the location, if available */
    @SerialName("region")
    val region: String?,

    /* Location country */
    @SerialName("country")
    val country: String?,

    /* Time zone name */
    @SerialName("tz_id")
    val timeZoneId: String?,

    /* Local date and time in unix time */
    @SerialName("localtime_epoch")
    val localtimeEpoch: Int?,

    /* Local date and time */
    @SerialName("localtime")
    val localtime: String?,
)
