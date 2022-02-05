package com.hellguy39.hellweather.utils

import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather

data class WeatherData(
    var currentWeather: CurrentWeather = CurrentWeather(),
    var hourlyWeather: MutableList<HourlyWeather> = ArrayList(),
    var dailyWeather: MutableList<DailyWeather> = ArrayList()
)
