package com.hellguy39.hellweather.data.mapper

import com.hellguy39.hellweather.data.remote.dto.CurrentWeatherDto
import com.hellguy39.hellweather.data.remote.dto.OneCallWeatherDto
import com.hellguy39.hellweather.domain.model.CurrentWeather
import com.hellguy39.hellweather.domain.model.OneCallWeather

fun OneCallWeatherDto.toOneCallWeather() : OneCallWeather {
    return OneCallWeather(
        lat = lat,
        lon = lon,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        currentWeather = currentWeather?.toCurrentWeather()
    )
}

fun CurrentWeatherDto.toCurrentWeather() : CurrentWeather {
    return CurrentWeather(
        date = date,
        sunrise = sunrise,
        sunset = sunset,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds= clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust
    )
}