package com.hellguy39.hellweather.presentation.fragments.page

import android.content.res.Resources
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherPageCollapseBinding
import com.hellguy39.hellweather.domain.models.weather.HourlyWeather
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.usecase.format.FormatUseCases
import com.hellguy39.hellweather.domain.utils.MM_HG
import com.hellguy39.hellweather.domain.utils.Unit
import com.hellguy39.hellweather.glide.GlideApp
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.presentation.adapter.NextHoursAdapter
import com.hellguy39.hellweather.utils.setToolbarNavigation
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


private const val WEATHER_DATA_ARG = "wd_arg"
private const val UNITS_ARG = "units_arg"

@AndroidEntryPoint
class WeatherPageFragment : Fragment(R.layout.fragment_weather_page_collapse) {

    @Inject
    lateinit var formatUserCases: FormatUseCases

    companion object {
        @JvmStatic
        fun newInstance(weatherData: WeatherData, units: String) = WeatherPageFragment().apply {
            arguments = Bundle().apply {
                putParcelable(WEATHER_DATA_ARG,weatherData)
                putString(UNITS_ARG, units)
            }
        }
    }

    private lateinit var _binding: FragmentWeatherPageCollapseBinding
    private val weatherData = MutableLiveData<WeatherData>()
    private val units = MutableLiveData<String>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("DEBUG", "HERE")
        CoroutineScope(Dispatchers.IO).launch {
            var wd = WeatherData()
            var unit = Unit.Metric.name
            arguments?.let {
                 wd = it.getParcelable<WeatherData>(WEATHER_DATA_ARG) as WeatherData
                 unit = it.getString(UNITS_ARG) as String
            }
            withContext(Dispatchers.Main) {
                weatherData.value = wd
                units.value = unit
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = super.onCreateView(inflater, container, savedInstanceState)
        _binding = FragmentWeatherPageCollapseBinding.bind(view!!)
        _binding.root.visibility = View.INVISIBLE
        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)

        weatherData.observe(viewLifecycleOwner) { _weatherData ->
            if (_weatherData == null)
                return@observe

            CoroutineScope(Dispatchers.Main).launch {
                updateUI(_weatherData)
                setupAdapters(_weatherData)
                updateGraph(_weatherData)
                fade()
            }
        }

        return view
    }

    private fun fade() {
        _binding.root.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
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
            xAxisLabel.add(formatUserCases.formatTimeUseCase.invoke(list[n].dt))
        }

        _binding.graphView.xAxis.textColor = textColor
        _binding.graphView.xAxis.valueFormatter = object : ValueFormatter() {
            override fun getAxisLabel(value: Float, axis: AxisBase?): String {
                return xAxisLabel[value.toInt()]
            }
        }

        leftAxis.axisMaximum = 100f
        leftAxis.axisMinimum = 0f
        rightAxis.isEnabled = false
        leftAxis.textColor = textColor

        _binding.graphView.legend.isEnabled = false
        _binding.graphView.description.isEnabled = false
        _binding.graphView.animateXY(1000, 1000)
    }

    private fun setupAdapters(weatherData: WeatherData) {

        val listDays = weatherData.dailyWeather
        val listHours = weatherData.hourlyWeather

        val _units = units.value ?: Unit.Metric.name

        _binding.recyclerNextDays.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = NextDaysAdapter(context, listDays, _units, resources)
        }
        _binding.recyclerNextHours.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextHoursAdapter(context, listHours, _units, resources)
        }
    }

    private fun updateUI(weatherData: WeatherData) {

        val wm = weatherData.currentWeather
        val _units = units.value ?: Unit.Metric

        GlideApp.with(_binding.ivWeather.context)
            .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)

        _binding.collapseToolbar.title = wm.name

        when (_units) {
            Unit.Standard.name -> {
                //_binding.collapseToolbar.title = String.format(resources.getString(R.string.top_tittle_kelvin_text), wm.temp, wm.wDescription)
                _binding.tvMaxMinTemp.text = String.format(resources.getString(R.string.max_min_kelvin_text),wm.tempMax, wm.tempMin)
                _binding.tvTempFeelsLike.text = String.format(resources.getString(R.string.temp_feels_like_kelvin_text),wm.tempFeelsLike)
                _binding.tvDewPoint.text = String.format(resources.getString(R.string.dew_point_now_kelvin_text), wm.dewPoint)

            }
            Unit.Imperial.name -> {
                //_binding.collapseToolbar.title = String.format(resources.getString(R.string.top_tittle_degree_text), wm.temp, wm.wDescription)
                _binding.tvMaxMinTemp.text = String.format(resources.getString(R.string.max_min_degree_text),wm.tempMax, wm.tempMin)
                _binding.tvTempFeelsLike.text = String.format(resources.getString(R.string.temp_feels_like_degree_text),wm.tempFeelsLike)
                _binding.tvDewPoint.text = String.format(resources.getString(R.string.dew_point_now_fahrenheit_text), wm.dewPoint)

            }
            Unit.Metric.name -> {
                //_binding.collapseToolbar.title = String.format(resources.getString(R.string.top_tittle_degree_text), wm.temp, wm.wDescription)
                _binding.tvMaxMinTemp.text = String.format(resources.getString(R.string.max_min_degree_text),wm.tempMax, wm.tempMin)
                _binding.tvTempFeelsLike.text = String.format(resources.getString(R.string.temp_feels_like_degree_text),wm.tempFeelsLike)
                _binding.tvDewPoint.text = String.format(resources.getString(R.string.dew_point_now_celsius_text), wm.dewPoint)

            }
        }

        val tempDesignation = when (_units) {
            Unit.Standard.name -> resources.getString(R.string.kelvin)
            Unit.Metric.name -> resources.getString(R.string.celsius)
            Unit.Imperial.name -> resources.getString(R.string.fahrenheit)
            else -> resources.getString(R.string.degree)
        }

        _binding.tvDot.text = tempDesignation
        _binding.tvTemp.text = wm.temp //+ "°"

        _binding.tvWeather.text = wm.wDescription
        _binding.tvSunrise.text = formatUserCases.formatTimeUseCase.invoke(wm.sunrise)
        _binding.tvSunset.text = formatUserCases.formatTimeUseCase.invoke(wm.sunset)

        _binding.tvTempFeelsDescription.text =
            if (wm.tempFeelsLike.toInt() == wm.temp.toInt()
                || wm.tempFeelsLike.toInt() == wm.temp.toInt() - 1
                || wm.tempFeelsLike.toInt() == wm.temp.toInt() + 1)
                resources.getString(R.string.feels_about_the_same)
            else
                ""

        _binding.tvUV.text = wm.uvi.toInt().toString()
        _binding.tvUVDescription.text =
            if (wm.uvi.toInt() == 0 || wm.uvi.toInt() == 1 || wm.uvi.toInt() == 2)
                resources.getString(R.string.low)
            else if (wm.uvi.toInt() == 3 || wm.uvi.toInt() == 4 || wm.uvi.toInt() == 5)
                resources.getString(R.string.medium)
            else if (wm.uvi.toInt() == 6 || wm.uvi.toInt() == 7)
                resources.getString(R.string.high)
            else if (wm.uvi.toInt() == 8 || wm.uvi.toInt() == 9 || wm.uvi.toInt() == 10)
                resources.getString(R.string.very_high)
            else
                resources.getString(R.string.extreme)

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


        _binding.tvVisibility.text = String.format(resources.getString(R.string.visibility_text), (wm.visibility / 1000))

        _binding.tvHumidity.text = String.format(resources.getString(R.string.humidity_text),wm.humidity)

        _binding.tvPressure.text = String.format(resources.getString(R.string.pressure_text),(wm.pressure.toDouble() * MM_HG).toInt())

        _binding.tvWind.text = String.format(resources.getString(R.string.wind_speed_text),wm.windSpeed)
        _binding.tvWindDirection.text = String.format(resources.getString(R.string.wind_direction_text),wm.windDeg)
        _binding.tvWindGust.text = String.format(resources.getString(R.string.wind_gust_text),wm.windGust)
    }
}