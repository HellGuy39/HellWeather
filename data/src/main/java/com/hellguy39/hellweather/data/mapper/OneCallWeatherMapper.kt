package com.hellguy39.hellweather.data.mapper

import com.hellguy39.hellweather.data.remote.dto.*
import com.hellguy39.hellweather.domain.model.*

fun OneCallWeatherDto.toOneCallWeather(): OneCallWeather {
    return OneCallWeather(
        lat = lat,
        lon = lon,
        timezone = timezone,
        timezoneOffset = timezoneOffset,
        currentWeather = currentWeather?.toCurrentWeather(),
        dailyWeather = dailyWeather?.map { it.toDailyWeather() },
        hourlyWeather = hourlyWeather?.map { it.toHourlyWeather() },
        alerts = alerts?.map { it.toAlert() }
    )
}

fun CurrentWeatherDto.toCurrentWeather(): CurrentWeather {
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
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather?.map { it.toWeather() }
    )
}

fun DailyWeatherDto.toDailyWeather(): DailyWeather {
    return DailyWeather(
        date = date,
        sunrise = sunrise,
        sunset = sunset,
        moonrise = moonrise,
        moonSet = moonSet,
        moonPhase = moonPhase,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        uvi = uvi,
        rain = rain,
        pop = pop,
        clouds = clouds,
        temp = temp?.toTemp(),
        feelsLike = feelsLike?.toFeelsLike(),
        weather = weather?.map { it.toWeather() }
    )
}

fun TempDto.toTemp(): Temp {
    return Temp(
        day = day,
        min = min,
        max = max,
        night = night,
        eve = eve,
        morn = morn
    )
}

fun FeelsLikeDto.toFeelsLike(): FeelsLike {
    return FeelsLike(
        day = day,
        night = night,
        eve = eve,
        morn = morn,
    )
}

fun HourlyWeatherDto.toHourlyWeather(): HourlyWeather {
    return HourlyWeather(
        date = date,
        temp = temp,
        feelsLike = feelsLike,
        pressure = pressure,
        humidity = humidity,
        dewPoint = dewPoint,
        uvi = uvi,
        clouds = clouds,
        visibility = visibility,
        windSpeed = windSpeed,
        windDeg = windDeg,
        windGust = windGust,
        weather = weather?.map { it.toWeather() },
        pop = pop
    )
}

fun WeatherDto.toWeather(): Weather {
    return Weather(
        id = id,
        main = main,
        description = description,
        icon = icon,
    )
}

fun AlertDto.toAlert() : Alert {
    return Alert(
        senderName = senderName,
        event = event,
        description = description,
        start = start,
        end = end,
        tags = tags,
    )
}
