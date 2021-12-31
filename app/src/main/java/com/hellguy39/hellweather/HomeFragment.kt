package com.hellguy39.hellweather

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.preference.PreferenceManager
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.models.CurrentWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
//import okhttp3.*
import com.google.gson.JsonObject
import com.hellguy39.hellweather.models.DailyWeather
import com.hellguy39.hellweather.models.HourlyWeather
import com.hellguy39.hellweather.models.UserLocation
import com.hellguy39.hellweather.retrofit.Common
import com.hellguy39.hellweather.retrofit.RetrofitServices
import kotlinx.coroutines.Dispatchers
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var usrLoc: UserLocation
    private lateinit var mService: RetrofitServices

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mService = Common.retrofitServices
        usrLoc = getUserLocation()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)
//        binding.rootView.setOnRefreshListener {
//            onRefresh()
//        }
        if (usrLoc.cityName != "N/A") {
            CoroutineScope(Dispatchers.IO).launch {
                request()
            }
        }

    }

    private fun request() {
        mService.getWeatherOneCall(usrLoc.lat.toDouble(),usrLoc.lon.toDouble(),"minutely,alerts","metric", OPEN_WEATHER_API_KEY).enqueue(object : Callback<JsonObject> {
            override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                if (response.isSuccessful)
                {
                    val jObj: JsonObject? = response.body()
                    if (jObj != null)
                    {
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
                            it.wDescription = wt.get("description").asString.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
                            it.icon = wt.get("icon").asString
                        }

                        updateUI(currentWeather)
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

    override fun onStart() {
        super.onStart()
        //onRefresh()
    }

    private fun getUserLocation(): UserLocation {
        val usrLoc = UserLocation()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        usrLoc.cityName = sharedPreferences.getString("cityName", "N/A").toString()
        usrLoc.lat = sharedPreferences.getString("lat", "0").toString()
        usrLoc.lon = sharedPreferences.getString("lon", "0").toString()
        return usrLoc
    }

    @SuppressLint("SetTextI18n")
    private fun updateUI(wm : CurrentWeather) {
        CoroutineScope(Main).launch {
            //binding.rootView.isRefreshing = false

            //Center
            Glide.with(this@HomeFragment)
                .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
                .centerCrop()
                .into(binding.ivWeather)

//            when (wm.wMain) {
//                "Clouds" -> {}
//                "Clear" -> {}
//                //"Atmosphere" -> {}
//                "Snow" -> {}
//                "Rain" -> {}
//                "Drizzle" -> {}
//                "Thunderstorm" -> {}
//            }

            binding.tvTemp.text = wm.temp + "°"
            //binding.tvMaxTemp.text = wm.tempMax
            //binding.tvMinTemp.text = wm.tempMin
            binding.tvWeather.text = wm.wDescription
            //Top
            binding.tvUpdateTime.text = wm.dt
            binding.tvCity.text = usrLoc.cityName
            //Details
            binding.tvSunrise.text = wm.sunrise
            binding.tvSunset.text = wm.sunset
            binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
            binding.tvHumidity.text = wm.humidity + "%"
            binding.tvPressure.text = wm.pressure + "hPa"
            binding.tvWind.text = wm.windSpeed + "m/s"

        }
    }
}