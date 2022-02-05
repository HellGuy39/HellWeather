package com.hellguy39.hellweather.presentation.fragments.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.models.GraphData
import com.broooapps.graphview.models.PointMap
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherPageBinding
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.presentation.adapter.NextHoursAdapter
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.WeatherData
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*

class WeatherPageFragment(
    private val weatherData: WeatherData
    ): Fragment(R.layout.fragment_weather_page) {

    /*companion object {
        fun newInstance(weatherData: WeatherData) = WeatherPageFragment(weatherData)
    }*/

    private lateinit var _binding: FragmentWeatherPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherPageBinding.bind(view)

        CoroutineScope(Default).launch {
            withContext(Main) {
                confGraph()
                updateUI(weatherData)
                updateRecyclersView(weatherData)
                updateGraph(weatherData)
            }
        }
    }

    private fun confGraph() {
        _binding.graphView.configure(
            CurveGraphConfig.Builder(context)
                .setAxisColor(R.color.white)
                .setIntervalDisplayCount(12)
                .setVerticalGuideline(12)
                .setHorizontalGuideline(5)
                .setGuidelineColor(R.color.Gray)
                .setNoDataMsg(getString(R.string.loading))
                .setxAxisScaleTextColor(R.color.white)
                .setyAxisScaleTextColor(R.color.white)
                .setAnimationDuration(2000)
                .build())
    }

    private fun updateGraph(weatherData: WeatherData) {

        val list: List<HourlyWeather> = weatherData.hourlyWeather

        val pointMap = PointMap()
        pointMap.addPoint(0, list[0].pop.toInt())
        pointMap.addPoint(1, list[1].pop.toInt())
        pointMap.addPoint(2, list[2].pop.toInt())
        pointMap.addPoint(3, list[3].pop.toInt())
        pointMap.addPoint(4, list[4].pop.toInt())
        pointMap.addPoint(5, list[5].pop.toInt())
        pointMap.addPoint(6, list[6].pop.toInt())
        pointMap.addPoint(7, list[7].pop.toInt())
        pointMap.addPoint(8, list[8].pop.toInt())
        pointMap.addPoint(9, list[9].pop.toInt())
        pointMap.addPoint(10, list[10].pop.toInt())
        pointMap.addPoint(11, list[11].pop.toInt())


        val gd = GraphData.builder(context)
            .setPointMap(pointMap)
            .setGraphStroke(R.color.White)
            .animateLine(true)
            .setGraphGradient(R.color.White, R.color.transparent)
            .build()

        CoroutineScope(Main).launch {
            delay(250L)
            _binding.graphView.setData(12, 100, gd)
        }

    }

    private fun updateRecyclersView(weatherData: WeatherData) = CoroutineScope(Main).launch {

        val listDays: MutableList<DailyWeather> = weatherData.dailyWeather
        val listHours: MutableList<HourlyWeather> = weatherData.hourlyWeather

        _binding.recyclerNextDays.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextDaysAdapter(context, listDays)
        }
        _binding.recyclerNextHours.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextHoursAdapter(context, listHours)
        }
    }

    private fun updateUI(weatherData: WeatherData) {

        val wm = weatherData.currentWeather

        Glide.with(this)
        .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)

        _binding.tvTextTop.text = wm.temp + "°" + " | " + wm.wDescription

        _binding.tvDot.text = "°"
        _binding.tvTemp.text = wm.temp //+ "°"
        _binding.tvMaxMinTemp.text = "Max.: ${wm.tempMax}°, min.: ${wm.tempMin}°"
        _binding.tvWeather.text = wm.wDescription
        _binding.tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunrise * 1000))
        _binding.tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunset * 1000))

        //Feels like
        _binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
        _binding.tvTempFeelsDescription.text =
            if (wm.tempFeelsLike.toInt() == wm.temp.toInt()
                || wm.tempFeelsLike.toInt() == wm.temp.toInt() - 1
                || wm.tempFeelsLike.toInt() == wm.temp.toInt() + 1)
                "Feels about the same"
            /*else if (wm.tempFeelsLike.toInt() <= wm.temp.toInt())
                "Feels colder with the wind"*/
            else
                ""

        // UV index
        _binding.tvUV.text = wm.uvi.toInt().toString()
        _binding.tvUVDescription.text =
            if (wm.uvi.toInt() == 0 || wm.uvi.toInt() == 1 || wm.uvi.toInt() == 2)
                "Low"
            else if (wm.uvi.toInt() == 3 || wm.uvi.toInt() == 4 || wm.uvi.toInt() == 5)
                "Medium"
            else if (wm.uvi.toInt() == 6 || wm.uvi.toInt() == 7)
                "High"
            else if (wm.uvi.toInt() == 8 || wm.uvi.toInt() == 9 || wm.uvi.toInt() == 10)
                "Very high"
            else
                "Extreme"

        _binding.vUVColor.backgroundTintList = if (wm.uvi.toInt() == 0 || wm.uvi.toInt() == 1 || wm.uvi.toInt() == 2)
            ResourcesCompat.getColorStateList(resources, R.color.green_A400, null)
        else if (wm.uvi.toInt() == 3 || wm.uvi.toInt() == 4 || wm.uvi.toInt() == 5)
            ResourcesCompat.getColorStateList(resources, R.color.yellow_A400, null)
        else if (wm.uvi.toInt() == 6 || wm.uvi.toInt() == 7)
            ResourcesCompat.getColorStateList(resources, R.color.orange_A400, null)
        else if (wm.uvi.toInt() == 8 || wm.uvi.toInt() == 9 || wm.uvi.toInt() == 10)
            ResourcesCompat.getColorStateList(resources, R.color.red_A400, null)
        else
            ResourcesCompat.getColorStateList(resources, R.color.deep_purple_A400, null)


        //Visibility
        _binding.tvVisibility.text = (wm.visibility / 1000).toString() + " km"
        /*_binding.tvVisibilityDescription.text = if ((wm.visibility / 1000) >= 7 && (wm.visibility / 1000) <= 10)
            "Visibility reduced due to light haze"
        else if ((wm.visibility / 1000) >= 11 && (wm.visibility / 1000) <= 13)
            "Now it's clear"
        else if ((wm.visibility / 1000) >= 14)
            "Now it's quite clear"
        else ""*/

        //Humidity
        _binding.tvHumidity.text = wm.humidity + "%"
        _binding.tvDewPoint.text = "Dew point now: " + wm.dewPoint + "°"

        //Pressure
        _binding.tvPressure.text = wm.pressure + " hPa"

        //Wind
        _binding.tvWind.text = wm.windSpeed + " m/s"
        _binding.tvWindDirection.text = "Direction: " + wm.windDeg + "°"
        _binding.tvWindGust.text = "Gust: " + wm.windGust + " m/s"
    }
}