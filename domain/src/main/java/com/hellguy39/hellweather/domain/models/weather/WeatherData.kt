package com.hellguy39.hellweather.domain.models.weather

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class WeatherData(
    var currentWeather: CurrentWeather = CurrentWeather(),
    var hourlyWeather: MutableList<HourlyWeather> = ArrayList(),
    var dailyWeather: MutableList<DailyWeather> = ArrayList(),
) : Parcelable
