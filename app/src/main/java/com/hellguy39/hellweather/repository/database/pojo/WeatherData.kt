package com.hellguy39.hellweather.repository.database.pojo

import java.io.Serializable

data class WeatherData(
    var currentWeather: CurrentWeather = CurrentWeather(),
    var hourlyWeather: MutableList<HourlyWeather> = ArrayList(),
    var dailyWeather: MutableList<DailyWeather> = ArrayList()
) : Serializable
