package com.hellguy39.hellweather.core.model

data class Location(
    /* Latitude in decimal degree */
    val lat: Double?,

    /* Longitude in decimal degree */
    val lon: Double?,

    /* Location name */
    val name: String?,

    /* Region or state of the location, if available */
    val region: String?,

    /* Location country */
    val country: String?,

    /* Time zone name */
    val timeZoneId: String?,

    /* Local date and time in unix time */
    val localtimeEpoch: Int?,

    /* Local date and time */
    val localtime: String?,
)
