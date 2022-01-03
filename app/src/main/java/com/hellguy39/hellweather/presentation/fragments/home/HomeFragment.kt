package com.hellguy39.hellweather.presentation.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.CurveGraphView
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.launch
//import okhttp3.*
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.absoluteValue
import com.broooapps.graphview.models.PointMap

import com.broooapps.graphview.models.GraphData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext())).get(HomeViewModel::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? = inflater.inflate(R.layout.fragment_home, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)

        binding.fabMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }

        binding.graphView.configure(CurveGraphConfig.Builder(context)
            .setAxisColor(R.color.White)
            .setIntervalDisplayCount(6)
            .setVerticalGuideline(5)
            .setHorizontalGuideline(5)
            .setGuidelineColor(R.color.Gray)
            .setNoDataMsg("Loading...")
            .setxAxisScaleTextColor(R.color.white)
            .setyAxisScaleTextColor(R.color.white)
            .setAnimationDuration(2000)
            .build())

        val pointMap = PointMap()
        pointMap.addPoint(0, 50)
        pointMap.addPoint(1, 55)
        pointMap.addPoint(2, 82)
        pointMap.addPoint(3, 30)
        pointMap.addPoint(4, 19)
        pointMap.addPoint(5, 25)

        val gd = GraphData.builder(context)
            .setPointMap(pointMap)
            .setGraphStroke(R.color.White)
            .animateLine(true)
            .setGraphGradient(R.color.White, R.color.transparent)
            .build()

        CoroutineScope(Main).launch {
            delay(1000L)
            binding.graphView.setData(6, 100, gd)
        }

        viewModel.currentWeatherLive.observe(this, {
            updateUI(it)
        })

        viewModel.dailyWeatherLive.observe(this, {
            it.removeAt(0) // Delete element 0, because it is today
            updateRecyclerView(it)
        })
    }



    override fun onStart() {
        super.onStart()

        if (viewModel.isUserLocationLive.value == true)
        {
            viewModel.requestToApi()
        }
        else
        {

        }
    }

    private fun updateRecyclerView(list: MutableList<DailyWeather>) {
        binding.recyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextDaysAdapter(context, list)
        }
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

            val isDay = if (wm.dt > wm.sunset) {
                false
            } else wm.dt > wm.sunrise

            //3600 - 1 hour
            val startSunset : Long = wm.sunset - 3600
            val endSunset : Long = wm.sunset + 3600

            val startSunrise : Long = wm.sunrise - 3600
            val endSunrise : Long = wm.sunrise + 3600

            val isSunriseOrSunset = if (wm.dt > startSunset && wm.dt < endSunset)
                true
            else
                if (wm.dt > startSunrise && wm.dt < endSunrise) true
            else
                false

            if (isDay)
            {
                when (wm.wMain) {
                    "Clouds" -> { binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_day) }
                    "Clear" -> {
                        if (isSunriseOrSunset)
                            binding.rootView.setBackgroundResource(R.drawable.gradient_sunset_or_sunrise)
                        else
                            binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)
                    }
                    "Mist" -> {}
                    "Smoke" -> {}
                    "Haze" -> {}
                    "Dust" -> {}
                    "Fog" -> {}
                    "Sand" -> {}
                    "Ash" -> {}
                    "Squall" -> {}
                    "Tornado" -> {}
                    "Snow" -> { binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day) }
                    "Rain" -> {}
                    "Drizzle" -> {}
                    "Thunderstorm" -> {}
                }
            }
            else {
                when (wm.wMain) {
                    "Clouds" -> { binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night) }
                    "Clear" -> {
                        if (isSunriseOrSunset)
                            binding.rootView.setBackgroundResource(R.drawable.gradient_sunset_or_sunrise)
                        else
                            binding.rootView.setBackgroundResource(R.drawable.gradient_clear_night)

                    }
                    "Mist" -> {}
                    "Smoke" -> {}
                    "Haze" -> {}
                    "Dust" -> {}
                    "Fog" -> {}
                    "Sand" -> {}
                    "Ash" -> {}
                    "Squall" -> {}
                    "Tornado" -> {}
                    "Snow" -> {}
                    "Rain" -> {}
                    "Drizzle" -> {}
                    "Thunderstorm" -> {}
                }
            }

            binding.tvTemp.text = wm.temp + "°"
            binding.tvMaxTemp.text = wm.tempMax + "°C"
            binding.tvMinTemp.text = wm.tempMin + "°C"
            binding.tvWeather.text = wm.wDescription
            //Top
            binding.tvUpdateTime.text = SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(Date(wm.dt * 1000))
            binding.tvCity.text = viewModel.usrLoc.cityName
            //Details
            binding.tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunrise * 1000))
            binding.tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunset * 1000))
            binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
            binding.tvHumidity.text = wm.humidity + "%"
            binding.tvPressure.text = wm.pressure + "hPa"
            binding.tvWind.text = wm.windSpeed + "m/s"

        }
    }
}