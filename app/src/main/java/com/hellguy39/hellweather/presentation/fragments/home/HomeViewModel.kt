package com.hellguy39.hellweather.presentation.fragments.home

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.repository.server.Common
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.collect
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject
import kotlin.collections.ArrayList

@HiltViewModel
class HomeViewModel @Inject constructor(
    repository: LocationRepository
) : ViewModel() {

    val isUpdate = MutableLiveData<Boolean>()
    val dailyWeatherLive = MutableLiveData<MutableList<DailyWeather>>()
    val hourlyWeatherLive = MutableLiveData<MutableList<HourlyWeather>>()
    val currentWeatherLive = MutableLiveData<CurrentWeather>()
    val userLocationsLive = MutableLiveData<List<UserLocation>>()

    private var mService: ApiService = Common.retrofitServices

    init {
        viewModelScope.launch {
            repository.getLocations().collect {
                userLocationsLive.value = it
            }
            isUpdate.value = false
        }
    }

    override fun onCleared() {
        super.onCleared()
        Log.d("DEBUG", "OnCleared")
    }

    private fun checkUserLocation(ul: UserLocation): Boolean {

        if (ul.requestName == "" &&
            ul.requestName == "N/A")
        {
            return false
        }

        if (ul.lat == "" &&
            ul.lat == "N/A")
        {
            return false
        }

        if (ul.lon == "" &&
            ul.lon == "N/A")
        {
            return false
        }

        return true
    }

    private suspend fun updatePojo(jObj: JsonObject) {

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

        withContext(Dispatchers.Main) {
            currentWeatherLive.value = currentWeather
            hourlyWeatherLive.value = hourlyWeather
            dailyWeatherLive.value = dailyWeather
            isUpdate.value = true
        }
    }

    suspend fun requestToApi(userLocation: UserLocation) = viewModelScope.launch {
        isUpdate.value = false

        mService.getWeatherOneCall(
            userLocation.lat.toDouble(),
            userLocation.lon.toDouble(),
            "minutely,alerts",
            "metric",
            OPEN_WEATHER_API_KEY
        ).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful)
                {
                    if (response.body() != null)
                    {
                        viewModelScope.launch {
                            updatePojo(response.body() as JsonObject)
                        }
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