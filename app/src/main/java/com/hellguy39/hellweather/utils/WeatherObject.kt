package com.hellguy39.hellweather.utils

import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather

object WeatherObject {
    var currentWeather = CurrentWeather()
    var hourlyWeather: MutableList<HourlyWeather> = ArrayList()
    var dailyWeather: MutableList<DailyWeather> = ArrayList()
}