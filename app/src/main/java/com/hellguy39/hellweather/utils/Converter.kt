package com.hellguy39.hellweather.utils

import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.pojo.*
import okhttp3.internal.userAgent
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Converter {

    fun checkRequest(jsonObject: JsonObject): String {
        return if (jsonObject.has("request")) {
            when (jsonObject.asJsonObject.get("request").asString) {
                FAILURE -> {
                    FAILURE
                }
                INCORRECT_OBJ -> {
                    INCORRECT_OBJ
                }
                else -> {
                    FAILURE
                }
            }
        } else {
            SUCCESSFUL
        }
    }

    fun toCurrentWeather(jsonObject: JsonObject) : CurrentWeather {
        val currentWeather = CurrentWeather()

        val weather = jsonObject.getAsJsonArray("weather").get(0)
        val main = jsonObject.getAsJsonObject("main")
        val wind = jsonObject.getAsJsonObject("wind")
        val sys = jsonObject.getAsJsonObject("sys")

        currentWeather.wMain = weather.asJsonObject.get("main").asString
        currentWeather.wDescription = weather.asJsonObject.get("description").asString.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(
                Locale.getDefault()
            ) else it.toString()
        }
        currentWeather.icon = weather.asJsonObject.get("icon").asString

        currentWeather.temp = main.get("temp").asDouble.toInt().toString()
        currentWeather.tempFeelsLike = main.get("feels_like").asDouble.toInt().toString()
        currentWeather.pressure = main.get("pressure").asString
        currentWeather.humidity = main.get("humidity").asString

        currentWeather.windSpeed = wind.get("speed").asString
        currentWeather.windDeg = wind.get("deg").asString
        if (wind.get("gust")?.asDouble != null)
            currentWeather.windGust = wind.get("gust").asDouble
        else
            currentWeather.windGust = 0.0

        currentWeather.dt = jsonObject.get("dt").asLong
        currentWeather.visibility = jsonObject.get("visibility").asInt

        currentWeather.sunrise = sys.get("sunrise").asLong
        currentWeather.sunset = sys.get("sunset").asLong

        currentWeather.name = jsonObject.get("name").asString

        return currentWeather
    }

    fun toUserLocation(jsonObject: JsonObject): UserLocation {
        val usrLoc = UserLocation()

        val coordinates = jsonObject.getAsJsonObject("coord")
        val sys = jsonObject.getAsJsonObject("sys")
        usrLoc.lat = coordinates.get("lat").asString
        usrLoc.lon = coordinates.get("lon").asString

        //usrLoc.requestName = input
        usrLoc.locationName = jsonObject.get("name").asString
        usrLoc.country = sys.get("country").asString
        usrLoc.cod = jsonObject.get("cod").asString
        //usrLoc.id = jObj.get("id").asInt
        usrLoc.timezone = jsonObject.get("timezone").asInt / 3600

        return usrLoc
    }

    fun toWeatherObject(jsonObject: JsonObject): WeatherData {

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

            if (current.get("wind_gust")?.asDouble != null)
                it.windGust = current.get("wind_gust").asDouble
            else
                it.windGust = 0.0

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
            dailyWeather[n].pop = obj.get("pop").asDouble * 100
            dailyWeather[n].icon = wt.get("icon").asString
            dailyWeather[n].max = obj.get("temp").asJsonObject.get("max").asDouble.toInt().toString()
            dailyWeather[n].min = obj.get("temp").asJsonObject.get("min").asDouble.toInt().toString()
            //dailyWeather[n].windSpeed = obj.get("wind_speed").asString
        }

        val data = WeatherData()

        data.currentWeather = currentWeather
        data.dailyWeather = dailyWeather
        data.hourlyWeather = hourlyWeather

        return data
    }
}