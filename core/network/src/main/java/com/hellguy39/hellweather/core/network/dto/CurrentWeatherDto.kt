package com.hellguy39.hellweather.core.network.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CurrentWeatherDto(

    /* Local time when the real time data was updated. */
    @SerialName("last_updated")
    val lastUpdated: String?,

    /* Local time when the real time data was updated in unix time. */
    @SerialName("last_updated_epoch")
    val lastUpdatedEpoch: Int?,

    /* Temperature in celsius */
    @SerialName("temp_c")
    val tempC: Double?,

    /* Temperature in fahrenheit */
    @SerialName("temp_f")
    val tempF: Double?,

    /* Feels like temperature in celsius */
    @SerialName("feelslike_c")
    val feelsLikeC: Double?,

    /* Feels like temperature in fahrenheit */
    @SerialName("feelslike_f")
    val feelsLikeF: Double?,

    @SerialName("condition")
    val conditionDto: ConditionDto?,

    /* Wind speed in miles per hour */
    @SerialName("wind_mph")
    val windMph: Double?,

    /* Wind speed in kilometer per hour */
    @SerialName("wind_kph")
    val windKph: Double?,

    /* Wind direction in degrees */
    @SerialName("wind_degree")
    val windDegree: Int?,

    /* Wind direction as 16 point compass. e.g.: NSW */
    @SerialName("wind_dir")
    val windDir: String?,

    /* Pressure in millibars */
    @SerialName("pressure_mb")
    val pressureMb: Double?,

    /* Pressure in inches */
    @SerialName("pressure_in")
    val pressureIn: Double?,

    /* Precipitation amount in millimeters */
    @SerialName("precip_mm")
    val precipitationMm: Double?,

    /* Precipitation amount in inches */
    @SerialName("precip_in")
    val precipitationIn: Double?,

    /* Humidity as percentage */
    @SerialName("humidity")
    val humidity: Int?,

    /* Cloud cover as percentage */
    @SerialName("cloud")
    val cloud: Int?,

    /* 1 = Yes 0 = No | Whether to show day condition icon or night icon */
    @SerialName("is_day")
    val isDay: Int?,

    /* 	UV Index */
    @SerialName("uv")
    val uv: Double?,

    /* Wind gust in miles per hour */
    @SerialName("gust_mph")
    val gustMph: Double?,

    /* Wind gust in kilometer per hour */
    @SerialName("gust_kph")
    val gustKph: Double?,
)

@Serializable
data class ConditionDto(

    /* Weather condition text */
    @SerialName("text")
    val text: String?,

    /* Weather icon url */
    @SerialName("icon")
    val icon: String?,

    /* Weather condition unique code */
    @SerialName("code")
    val code: Int?,
)



