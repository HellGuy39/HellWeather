package com.hellguy39.hellweather.core.data.mapper

import com.hellguy39.hellweather.core.model.Condition
import com.hellguy39.hellweather.core.model.CurrentWeather
import com.hellguy39.hellweather.core.model.Location
import com.hellguy39.hellweather.core.model.RealtimeWeather
import com.hellguy39.hellweather.core.network.RealtimeWeatherDto
import com.hellguy39.hellweather.core.network.dto.ConditionDto
import com.hellguy39.hellweather.core.network.dto.CurrentWeatherDto
import com.hellguy39.hellweather.core.network.dto.LocationDto

fun RealtimeWeatherDto.toRealtimeWeather(): RealtimeWeather {
    return RealtimeWeather(
        currentWeather = currentWeatherDto?.toCurrentWeather(),
        location = locationDto?.toLocation()
    )
}

fun CurrentWeatherDto.toCurrentWeather(): CurrentWeather {
    return CurrentWeather(
        lastUpdated = lastUpdated,
        lastUpdatedEpoch = lastUpdatedEpoch,
        tempC = tempC,
        tempF = tempF,
        feelsLikeC = feelsLikeC,
        feelsLikeF = feelsLikeF,
        condition = conditionDto?.toCondition(),
        windMph = windMph,
        windKph = windKph,
        windDegree = windDegree,
        windDir = windDir,
        pressureMb = pressureMb,
        pressureIn = pressureIn,
        precipitationMm = precipitationMm,
        precipitationIn = precipitationIn,
        humidity = humidity,
        cloud = cloud,
        isDay = isDay == 1,
        uv = uv,
        gustMph = gustMph,
        gustKph = gustKph,
    )
}

fun LocationDto.toLocation(): Location {
    return Location(
        lat = lat,
        lon = lon,
        name = name,
        region = region,
        country = country,
        timeZoneId = timeZoneId,
        localtimeEpoch = localtimeEpoch,
        localtime = localtime,
    )
}

fun ConditionDto.toCondition(): Condition {
    return Condition(
        text = text,
        icon = icon,
        code = code,
    )
}