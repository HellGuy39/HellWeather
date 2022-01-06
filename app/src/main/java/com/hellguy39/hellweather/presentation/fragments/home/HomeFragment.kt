package com.hellguy39.hellweather.presentation.fragments.home

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.models.GraphData
import com.broooapps.graphview.models.PointMap
import com.bumptech.glide.Glide
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Default
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*


class HomeFragment : Fragment(R.layout.fragment_home) {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(this, HomeViewModelFactory(requireContext()))[HomeViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding = FragmentHomeBinding.bind(view)

        //binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)
        binding.fabMenu.setOnClickListener {
            (activity as MainActivity).openDrawer()
        }


    }

    override fun onStart() {
        super.onStart()

        confGraph()

        viewModel.hourlyWeatherLive.observe(this, {
            updateGraph(it)
        })

        viewModel.currentWeatherLive.observe(this, {
            updateUI(it)
        })

        viewModel.dailyWeatherLive.observe(this, {
            it.removeAt(0) // Delete element 0, because it is today
            CoroutineScope(Main).launch {
                updateRecyclerView(it)
            }
        })

        if (viewModel.isUserLocationLive.value == true)
        {
            CoroutineScope(Dispatchers.Default).launch {
                viewModel.requestToApi()
            }
        }
        else
        {

        }

    }

    private fun confGraph() = CoroutineScope(Main).launch {
        binding.graphView.configure(CurveGraphConfig.Builder(context)
            .setAxisColor(R.color.White)
            .setIntervalDisplayCount(12)
            .setVerticalGuideline(12)
            .setHorizontalGuideline(5)
            .setGuidelineColor(R.color.Gray)
            .setNoDataMsg("Loading...")
            .setxAxisScaleTextColor(R.color.white)
            .setyAxisScaleTextColor(R.color.white)
            .setAnimationDuration(2000)
            .build())
    }

    private fun updateGraph(list: MutableList<HourlyWeather>) {

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

        Log.d("DEBUG", list[11].pop.toInt().toString())

        val gd = GraphData.builder(context)
            .setPointMap(pointMap)
            .setGraphStroke(R.color.White)
            .animateLine(true)
            .setGraphGradient(R.color.White, R.color.transparent)
            .build()

        CoroutineScope(Main).launch {
            delay(250L)
            binding.graphView.setData(12, 100, gd)
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

            /*val isDay = if (wm.dt > wm.sunset) {
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
*/
//            if (isDay)
//            {
//                when (wm.wMain) {
//                    "Clouds" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_day)}
//                    "Clear" -> {
//                        if (isSunriseOrSunset)
//                            binding.rootView.setBackgroundResource(R.drawable.gradient_sunset_or_sunrise)
//                        else
//                            binding.rootView.setBackgroundResource(R.drawable.gradient_clear_day)
//                    }
//                    "Mist" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Smoke" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Haze" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Dust" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Fog" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Sand" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Ash" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Squall" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Tornado" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Snow" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Rain" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Drizzle" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                    "Thunderstorm" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_snow_day)}
//                }
//            }
//            else {
//                when (wm.wMain) {
//                    "Clouds" -> { binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night) }
//                    "Clear" -> {
//                        if (isSunriseOrSunset)
//                            binding.rootView.setBackgroundResource(R.drawable.gradient_sunset_or_sunrise)
//                        else
//                            binding.rootView.setBackgroundResource(R.drawable.gradient_clear_night)
//
//                    }
//                    "Mist" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Smoke" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Haze" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Dust" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Fog" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Sand" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Ash" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Squall" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Tornado" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Snow" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Rain" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Drizzle" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                    "Thunderstorm" -> {binding.rootView.setBackgroundResource(R.drawable.gradient_cloudy_night)}
//                }
//            }

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