package com.hellguy39.hellweather.domain.models

import java.io.Serializable

data class WeatherData(
    var currentWeather: CurrentWeather = CurrentWeather(),
    var hourlyWeather: MutableList<HourlyWeather> = ArrayList(),
    var dailyWeather: MutableList<DailyWeather> = ArrayList(),
    var requestResult: String = "N/A",
    var errorBody: String = "N/A"
) : Serializable
