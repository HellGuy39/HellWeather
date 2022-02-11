package com.hellguy39.hellweather.presentation.fragments.page

import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.util.TypedValue
import android.view.View
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.github.mikephil.charting.model.GradientColor
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherPageBinding
import com.hellguy39.hellweather.glide.GlideApp
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.presentation.adapter.NextHoursAdapter
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.WeatherData
import com.hellguy39.hellweather.utils.IMPERIAL
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import com.hellguy39.hellweather.utils.STANDARD
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.*


private const val WEATHER_DATA_ARG = "wd_arg"

class WeatherPageFragment() : Fragment(R.layout.fragment_weather_page) {

    companion object {
        @JvmStatic
        fun newInstance(weatherData: WeatherData) = WeatherPageFragment().apply {
            arguments = Bundle().apply {
                putSerializable(WEATHER_DATA_ARG,weatherData)
            }
        }
    }

    private lateinit var _binding: FragmentWeatherPageBinding
    private lateinit var _weatherData: WeatherData
    private lateinit var units: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            _weatherData = it.getSerializable(WEATHER_DATA_ARG) as WeatherData
        }
        units = PreferenceManager.getDefaultSharedPreferences(requireContext())
            .getString(PREFS_UNITS, STANDARD).toString()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherPageBinding.bind(view)

        CoroutineScope(Default).launch {
            if (this@WeatherPageFragment::_weatherData.isInitialized) {
                withContext(Main) {
                    //confGraph()
                    updateUI(_weatherData)
                    updateRecyclersView(_weatherData)
                    updateGraph(_weatherData)
                }
            }
        }
    }

    private fun updateGraph(weatherData: WeatherData) {

        val list: List<HourlyWeather> = weatherData.hourlyWeather
        val values: ArrayList<Entry> = arrayListOf()

        val theme: Resources.Theme = context?.theme!!
        val lineColorValue = TypedValue()

        theme.resolveAttribute(R.attr.colorPrimary, lineColorValue, true)

        @ColorInt val textColor = _binding.tvChance.currentTextColor
        @ColorInt val lineColor = lineColorValue.data

        values.add(Entry(0f,list[0].pop.toFloat()))
        values.add(Entry(1f,list[1].pop.toFloat()))
        values.add(Entry(2f,list[2].pop.toFloat()))
        values.add(Entry(3f,list[3].pop.toFloat()))
        values.add(Entry(4f,list[4].pop.toFloat()))
        values.add(Entry(5f,list[5].pop.toFloat()))
        values.add(Entry(6f,list[6].pop.toFloat()))
        values.add(Entry(7f,list[7].pop.toFloat()))
        values.add(Entry(8f,list[8].pop.toFloat()))
        values.add(Entry(9f,list[9].pop.toFloat()))
        values.add(Entry(10f,list[10].pop.toFloat()))
        values.add(Entry(11f,list[11].pop.toFloat()))

        val lineDataSet = LineDataSet(values, "Chance of rain")

        lineDataSet.color = lineColor
        lineDataSet.setCircleColor(lineColor)
        lineDataSet.valueTextSize = 10f
        lineDataSet.lineWidth = 2f
        //lineDataSet.setGradientColor(Color.BLUE, Color.TRANSPARENT)
        lineDataSet.valueTextColor = textColor

        val dataSets: MutableList<ILineDataSet> = mutableListOf()

        dataSets.add(lineDataSet)

        val lineData = LineData(dataSets)

        _binding.graphView.data = lineData

        val leftAxis: YAxis = _binding.graphView.axisLeft
        val rightAxis: YAxis = _binding.graphView.axisRight

        //leftAxis.textColor = textColor

        val xAxisLabel: MutableList<String> = mutableListOf()

        for (n in list.indices) {
            xAxisLabel.add(SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(list[n].dt * 1000)))
        }

        _binding.graphView.xAxis.textColor = textColor
        _binding.graphView.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xAxisLabel[value.toInt()]
            }
        }


        //leftAxis.setDrawAxisLine(false)
        //rightAxis.setDrawAxisLine(false)
        //leftAxis.isEnabled = false
        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        rightAxis.isEnabled = false
        leftAxis.textColor = textColor

        _binding.graphView.legend.isEnabled = false
        _binding.graphView.description.isEnabled = false
        _binding.graphView.animateXY(1000, 1000)
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

        GlideApp.with(_binding.ivWeather.context)
            .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)

        /*Glide.with(this)
        .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)*/

        val tempDesignation = when (units) {
            STANDARD -> " K"
            METRIC -> "°C"
            IMPERIAL -> "°F"
            else -> "°"
        }

        _binding.tvTextTop.text = wm.temp + tempDesignation + " | " + wm.wDescription

        _binding.tvDot.text = tempDesignation
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
            ResourcesCompat.getColorStateList(resources, R.color.green_400, null)
        else if (wm.uvi.toInt() == 3 || wm.uvi.toInt() == 4 || wm.uvi.toInt() == 5)
            ResourcesCompat.getColorStateList(resources, R.color.yellow_400, null)
        else if (wm.uvi.toInt() == 6 || wm.uvi.toInt() == 7)
            ResourcesCompat.getColorStateList(resources, R.color.orange_400, null)
        else if (wm.uvi.toInt() == 8 || wm.uvi.toInt() == 9 || wm.uvi.toInt() == 10)
            ResourcesCompat.getColorStateList(resources, R.color.red_400, null)
        else
            ResourcesCompat.getColorStateList(resources, R.color.deep_purple_400, null)


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