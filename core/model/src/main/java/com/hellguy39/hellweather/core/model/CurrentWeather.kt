package com.hellguy39.hellweather.core.model

data class CurrentWeather(

    /* Local time when the real time data was updated. */
    val lastUpdated: String?,

    /* Local time when the real time data was updated in unix time. */
    val lastUpdatedEpoch: Int?,

    /* Temperature in celsius */
    val tempC: Double?,

    /* Temperature in fahrenheit */
    val tempF: Double?,

    /* Feels like temperature in celsius */
    val feelsLikeC: Double?,

    /* Feels like temperature in fahrenheit */
    val feelsLikeF: Double?,

    val condition: Condition?,

    /* Wind speed in miles per hour */
    val windMph: Double?,

    /* Wind speed in kilometer per hour */
    val windKph: Double?,

    /* Wind direction in degrees */
    val windDegree: Int?,

    /* Wind direction as 16 point compass. e.g.: NSW */
    val windDir: String?,

    /* Pressure in millibars */
    val pressureMb: Double?,

    /* Pressure in inches */
    val pressureIn: Double?,

    /* Precipitation amount in millimeters */
    val precipitationMm: Double?,

    /* Precipitation amount in inches */
    val precipitationIn: Double?,

    /* Humidity as percentage */
    val humidity: Int?,

    /* Cloud cover as percentage */
    val cloud: Int?,

    /* 1 = Yes 0 = No | Whether to show day condition icon or night icon */
    val isDay: Boolean?,

    /* 	UV Index */
    val uv: Double?,

    /* Wind gust in miles per hour */
    val gustMph: Double?,

    /* Wind gust in kilometer per hour */
    val gustKph: Double?,
)

data class Condition(

    /* Weather condition text */
    val text: String?,

    /* Weather icon url */
    val icon: String?,

    /* Weather condition unique code */
    val code: Int?,
)