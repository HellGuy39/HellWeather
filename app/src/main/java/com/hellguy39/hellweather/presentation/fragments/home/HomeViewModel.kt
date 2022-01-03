package com.hellguy39.hellweather.presentation.fragments.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.repository.server.Common
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeViewModel(
    getUserLocation: UserLocation
) : ViewModel() {

    val dailyWeatherLive = MutableLiveData<MutableList<DailyWeather>>()
    val hourlyWeatherLive = MutableLiveData<MutableList<HourlyWeather>>()
    val currentWeatherLive = MutableLiveData<CurrentWeather>()

    val userLocationLive = MutableLiveData<UserLocation>()
    val isUserLocationLive = MutableLiveData<Boolean>()

    var usrLoc: UserLocation = getUserLocation
    private var mService: ApiService = Common.retrofitServices

    init {
        userLocationLive.value = getUserLocation
        isUserLocationLive.value = checkUserLocation(userLocationLive.value!!) //true - всё окей, false - всё плохо
    }

    private fun checkUserLocation(ul: UserLocation): Boolean {

        if (ul.cityName == null &&
            ul.cityName == "N/A")
        {
            return false
        }

        if (ul.lat == null &&
            ul.lat == "N/A")
        {
            return false
        }

        if (ul.lon == null &&
            ul.lon == "N/A")
        {
            return false
        }

        return true
    }

    private fun updatePojo(jObj: JsonObject) {

        val currentWeather = CurrentWeather()
        val hourlyWeather: MutableList<HourlyWeather> = ArrayList()
        val dailyWeather: MutableList<DailyWeather> = ArrayList()

        val current = jObj.getAsJsonObject("current")
        val hourly = jObj.getAsJsonArray("hourly")
        val daily = jObj.getAsJsonArray("daily")

        currentWeather.let {
            it.dt = current.get("dt").asLong
            it.sunrise = current.get("sunrise").asLong
            it.sunset = current.get("sunset").asLong
            it.temp = current.get("temp").asDouble.toInt().toString()
            it.tempFeelsLike = current.get("feels_like").asDouble.toInt().toString()
            it.pressure = current.get("pressure").asString
            it.humidity = current.get("humidity").asString
            it.windSpeed = current.get("wind_speed").asString
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

            hourlyWeather.add(n, HourlyWeather())

            hourlyWeather[n].temp = obj.get("temp").asString
            hourlyWeather[n].dt = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(obj.get("dt").asLong * 1000))
            hourlyWeather[n].tempFeelsLike = obj.get("feels_like").asString
            hourlyWeather[n].humidity = obj.get("humidity").asString
            hourlyWeather[n].pressure = obj.get("pressure").asString
            hourlyWeather[n].pop = obj.get("pop").asString
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

        currentWeatherLive.value = currentWeather
        hourlyWeatherLive.value = hourlyWeather
        dailyWeatherLive.value = dailyWeather
    }

    fun requestToApi() {
        mService.getWeatherOneCall(usrLoc.lat.toDouble(),usrLoc.lon.toDouble(),"minutely,alerts","metric", OPEN_WEATHER_API_KEY).enqueue(object :
            Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful)
                {
                    if (response.body() != null)
                    {
                        updatePojo(response.body() as JsonObject)
                    }
                }
                else
                {

                }

            }

            override fun onFailure(call: Call<JsonObject>, t: Throwable) {

            }

        })
    }
}