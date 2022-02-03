package com.hellguy39.hellweather.utils

import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Converter {

    fun toWeatherObject(jsonObject: JsonObject): WeatherObject {

        val currentWeather = CurrentWeather()
        val hourlyWeather: MutableList<HourlyWeather> = ArrayList()
        val dailyWeather: MutableList<DailyWeather> = ArrayList()

        val current = jsonObject.getAsJsonObject("current")
        val hourly = jsonObject.getAsJsonArray("hourly")
        val daily = jsonObject.getAsJsonArray("daily")

        currentWeather.let {
            it.dt = current.get("dt").asLong
            it.sunrise = current.get("sunrise").asLong
            it.sunset = current.get("sunset").asLong
            it.temp = current.get("temp").asDouble.toInt().toString()
            it.tempFeelsLike = current.get("feels_like").asDouble.toInt().toString()
            it.pressure = current.get("pressure").asString
            it.humidity = current.get("humidity").asString
            it.windSpeed = current.get("wind_speed").asString
            it.windDeg = current.get("wind_deg").asString
            it.windGust = current.get("wind_gust").asString
            it.dewPoint = current.get("dew_point").asFloat.toInt().toString()
            it.uvi = current.get("uvi").asDouble
            it.visibility = current.get("visibility").asInt

            val wt = current.getAsJsonArray("weather").get(0).asJsonObject
            it.wMain = wt.get("main").asString
            it.wDescription = wt.get("description").asString.replaceFirstChar {
                if (it.isLowerCase()) it.titlecase(
                    Locale.getDefault()
                ) else it.toString()
            }
            it.icon = wt.get("icon").asString

            val obj = daily[0].asJsonObject // if index = 0 -> today
            it.tempMax = obj.get("temp").asJsonObject.get("max").asDouble.toInt().toString()
            it.tempMin = obj.get("temp").asJsonObject.get("min").asDouble.toInt().toString()

        }

        for (n in 0 until hourly.asJsonArray.size())
        {
            val obj = hourly[n].asJsonObject
            val wt = obj.getAsJsonArray("weather").get(0).asJsonObject

            hourlyWeather.add(n, HourlyWeather())

            hourlyWeather[n].icon = wt.get("icon").asString
            hourlyWeather[n].temp = obj.get("temp").asInt.toString()
            hourlyWeather[n].dt = obj.get("dt").asLong
            hourlyWeather[n].tempFeelsLike = obj.get("feels_like").asString
            hourlyWeather[n].humidity = obj.get("humidity").asString
            hourlyWeather[n].pressure = obj.get("pressure").asString
            hourlyWeather[n].pop = obj.get("pop").asDouble * 100
            hourlyWeather[n].windSpeed = obj.get("wind_speed").asString
        }

        for (n in 0 until daily.asJsonArray.size())
        {
            val obj = daily[n].asJsonObject
            val wt = obj.getAsJsonArray("weather").get(0).asJsonObject

            dailyWeather.add(n, DailyWeather())

            //dailyWeather[n].temp = obj.get("temp").asString
            dailyWeather[n].dt = SimpleDateFormat("E", Locale.getDefault()).format(Date(obj.get("dt").asLong * 1000))
            //dailyWeather[n].tempFeelsLike = obj.get("feels_like").asString
            dailyWeather[n].humidity = obj.get("humidity").asString
            //dailyWeather[n].pressure = obj.get("pressure").asString
            dailyWeather[n].pop = obj.get("pop").asString
            dailyWeather[n].icon = wt.get("icon").asString
            dailyWeather[n].max = obj.get("temp").asJsonObject.get("max").asDouble.toInt().toString()
            dailyWeather[n].min = obj.get("temp").asJsonObject.get("min").asDouble.toInt().toString()
            //dailyWeather[n].windSpeed = obj.get("wind_speed").asString
        }

        val obj = WeatherObject

        WeatherObject.currentWeather = currentWeather
        WeatherObject.dailyWeather = dailyWeather
        WeatherObject.hourlyWeather = hourlyWeather

        return obj
    }
}