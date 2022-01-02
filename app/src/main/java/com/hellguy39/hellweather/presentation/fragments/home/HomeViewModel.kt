package com.hellguy39.hellweather.presentation.fragments.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.preference.PreferenceManager
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
        val hourlyWeather: List<HourlyWeather> = ArrayList()
        val dailyWeather: List<DailyWeather> = ArrayList()

        val current = jObj.getAsJsonObject("current")
        val hourly = jObj.getAsJsonArray("hourly")
        val daily = jObj.getAsJsonArray("daily")

        currentWeather.let {
            it.dt = SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(Date(current.get("dt").asLong * 1000))
            it.sunrise = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(current.get("sunrise").asLong * 1000))
            it.sunset = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(current.get("sunset").asLong * 1000))
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
        }

/*        for (n in 0..48) {
            Log.d("DEBUG", "For: $n")
            val obj = hourly[n].asJsonObject
            hourlyWeather[n].temp = obj.get("").asString
        }*/

        currentWeatherLive.value = currentWeather
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