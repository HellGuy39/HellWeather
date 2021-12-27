package com.hellguy39.hellweather

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.utils.OW_API_KEY
import com.hellguy39.hellweather.utils.WeatherModel
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import okhttp3.*
import org.json.JSONObject
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding

    private var okHttpClient: OkHttpClient = OkHttpClient()
    private val city = "Krasnodar"
    private val url = "https://api.openweathermap.org/data/2.5/weather?q=$city&units=metric&appid=$OW_API_KEY"
    private val LOG_HOME_FRAGMENT = "HomeFragment"

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)
        binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)
//        binding.rootView.setOnRefreshListener {
//            onRefresh()
//        }
        binding.rootView

    }

    override fun onStart() {
        super.onStart()
        onRefresh()
    }

    private fun onRefresh() {
        CoroutineScope(IO).launch {
            val request: Request = Request
                .Builder()
                .url(url)
                .build()
            request(request)
        }
    }

    private fun updateUI(wm : WeatherModel) {
        CoroutineScope(Main).launch {
            Log.i(LOG_HOME_FRAGMENT, "updateUI() was called")
            //binding.rootView.isRefreshing = false
            //Center
            Picasso.get()
                .load("https://openweathermap.org/img/wn/${wm.owIcon}@2x.png")
                .into(binding.ivWeather)

            binding.tvTemp.text = wm.temp
            binding.tvMaxTemp.text = wm.tempMax
            binding.tvMinTemp.text = wm.tempMin
            binding.tvWeather.text = wm.wDescription
            //Top
            binding.tvUpdateTime.text = wm.updateTime
            binding.tvCity.text = wm.city
            //Details
            binding.tvSunrise.text = wm.sunrise
            binding.tvSunset.text = wm.sunset
            binding.tvTempFeelsLike.text = wm.tempFeelsLike
            binding.tvHumidity.text = wm.humidity
            binding.tvPressure.text = wm.pressure
            binding.tvWind.text = wm.wind

        }
    }

    private fun request(request: Request){
        Log.i(LOG_HOME_FRAGMENT, "request() was called")
        val wm = WeatherModel()

        okHttpClient
            .newCall(request)
            .enqueue(object: Callback {
                override fun onFailure(call: Call, e: IOException)
                {

                }

                override fun onResponse(call: Call, response: Response)
                {

                    if (response.isSuccessful)
                    {
                        val result = response.body?.string()
                        result?.let { Log.i("LOG", it) }

                        val jsonObject = JSONObject(result)

                        val main = jsonObject.getJSONObject("main")
                        val sys = jsonObject.getJSONObject("sys")
                        val sunrise = sys.getLong("sunrise")
                        val sunset = sys.getLong("sunset")
                        val wind = jsonObject.getJSONObject("wind")
                        val time: Long = jsonObject.getLong("dt")
                        val weather = jsonObject.getJSONArray("weather").getJSONObject(0)
                        val sdf = SimpleDateFormat("E, HH:mm", Locale.getDefault())//.setTimeZone(TimeZone.getTimeZone("GMT"))//.format(Date(time * 1000))

                        wm.temp = main.getInt("temp").toString() + "°"
                        wm.tempFeelsLike = main.getInt("feels_like").toString() + "°"
                        wm.tempMax = main.getInt("temp_max").toString() + "° C"
                        wm.tempMin = main.getInt("temp_min").toString() + "° C"
                        wm.pressure = main.getInt("pressure").toString() + "hPa"
                        wm.humidity = main.getInt("humidity").toString() + "%"
                        wm.sunrise = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sunrise * 1000))
                        wm.sunset = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(sunset * 1000))
                        wm.wind = wind.getDouble("speed").toString() + " m/s"
                        wm.updateTime = sdf.format(Date(time * 1000))
                        wm.city = city
                        wm.wDescription = weather
                            .getString("description")
                            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }

                        wm.owIcon = weather.getString("icon")

                        updateUI(wm)
                    }
                    else
                    {

                    }
                }
            })
    }
}