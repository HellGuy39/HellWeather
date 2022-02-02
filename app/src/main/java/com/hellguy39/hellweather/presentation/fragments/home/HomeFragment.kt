package com.hellguy39.hellweather.presentation.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.tabs.TabLayout
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.MainActivityViewModel
import com.hellguy39.hellweather.presentation.adapter.WeatherPageAdapter
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home)/*,TabLayout.OnTabSelectedListener*/ {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var selectedTab: TabLayout.Tab? = null
    private lateinit var mainActivityViewModel: MainActivityViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        mainActivityViewModel = ViewModelProvider(activity as MainActivity)[MainActivityViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        //(activity as MainActivity).setToolbarTittle(getString(R.string.loading))
        //(activity as MainActivity).updateToolbarMenu(ENABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        mainActivityViewModel.statusLive.observe(activity as MainActivity) {
            if (it == SUCCESSFUL) {
                val list = mainActivityViewModel.weatherJsonListLive.value
                if (list != null) {
                    val pagerAdapter = WeatherPageAdapter(activity as MainActivity, list)
                    binding.viewPager.adapter = pagerAdapter
                }
            }
        }

        /*confGraph()
        binding.tabLayout.addOnTabSelectedListener(this)
        setObservers()*/

    }

    override fun onStart() {
        super.onStart()

    }

    /*fun onRefresh() {

        val tab = selectedTab

        if (tab != null) {
            updateIndicator(ENABLE)
            (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
            clearUI()

            CoroutineScope(Dispatchers.Default).launch {
                val id = tab.tag
                val location = viewModel.getLocation(id!!)
                viewModel.requestToApi(location)
            }
        }
    }

    private fun updateIndicator(action: String) {
        if (action == ENABLE) {
            binding.progressIndicator.visibility = View.VISIBLE
        }
        else
        {
            binding.progressIndicator.visibility = View.INVISIBLE
        }
    }*/

    /*private fun setObservers() {

        viewModel.userLocationsLive.observe(viewLifecycleOwner) {
            for (n in it.indices) {
                val tab = binding.tabLayout.newTab()
                tab.text = it[n].locationName
                tab.tag = it[n].id
                *//*if (n == 0) {
                    tab.icon = (ResourcesCompat.getDrawable(resources, R.drawable.ic_round_home_24, null))
                }*//*
                binding.tabLayout.addTab(tab)
            }
        }

        viewModel.requestStatus.observe(viewLifecycleOwner) {
            when (it) {
                SUCCESSFUL -> {
                    onSuccessfulRequest()
                }
                ERROR -> {
                    onErrorRequest()
                }
                FAILURE -> {
                    onFailureRequest()
                }
                EMPTY_BODY -> {
                    onEmptyBodyRequest()
                }
                IN_PROGRESS -> {
                    onInProgressRequest()
                }
            }
        }*/

        /*viewModel.isUpdate.observe(this, {
            if (it == true) {
                val currentWeather = viewModel.currentWeatherLive.value
                val hourlyWeather = viewModel.hourlyWeatherLive.value
                val dailyWeather = viewModel.dailyWeatherLive.value

                if (currentWeather != null && hourlyWeather != null && dailyWeather != null) {
                    updateUI(currentWeather)
                    updateGraph(hourlyWeather)
                    dailyWeather.removeAt(0) // Delete element 0, because it is today
                    updateRecyclersView(dailyWeather, hourlyWeather)
                    updateIndicator(DISABLE)
                }
            }
        })
    }*/

    /*private fun onErrorRequest() {
        Log.d("DEBUG", "ERROR")
    }

    private fun onFailureRequest() {
        Snackbar.make(binding.rootLayout, R.string.on_failure, Snackbar.LENGTH_LONG)
            .setAction(R.string.try_again) {
                Log.d("DEBUG", "TRY AGAIN")
            }.show()
    }

    private fun onEmptyBodyRequest() {
        Log.d("DEBUG", "EMPTY")
    }


    private fun onInProgressRequest() {
        Log.d("DEBUG", "IN PROGRESS")
    }


    private fun onSuccessfulRequest() {
        val currentWeather = viewModel.currentWeatherLive.value
        val hourlyWeather = viewModel.hourlyWeatherLive.value
        val dailyWeather = viewModel.dailyWeatherLive.value

        if (currentWeather != null && hourlyWeather != null && dailyWeather != null) {
            updateUI(currentWeather)
            updateGraph(hourlyWeather)
            dailyWeather.removeAt(0) // Delete element 0, because it is today
            updateRecyclersView(dailyWeather, hourlyWeather)
            updateIndicator(DISABLE)
        }
    }

    private fun confGraph() {
        binding.graphView.configure(
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

    private fun updateRecyclersView(
        listDays: MutableList<DailyWeather>,
        listHours: MutableList<HourlyWeather>
    ) = CoroutineScope(Main).launch {
        binding.recyclerNextDays.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextDaysAdapter(context, listDays)
        }
        binding.recyclerNextHours.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = NextHoursAdapter(context, listHours)
        }
    }

    private fun clearUI() {
        CoroutineScope(Main).launch {
            Glide.with(this@HomeFragment).clear(binding.ivWeather)

            val na = getString(R.string.not_available)

            binding.tvTextTop.text = "--"
            binding.tvDot.text = ""
            binding.tvTemp.text = "--"
            binding.tvMaxMinTemp.text = ""
            binding.tvWeather.text = ""

            binding.tvSunrise.text = na
            binding.tvSunset.text = na
            binding.tvTempFeelsLike.text = na
            binding.tvHumidity.text = na
            binding.tvPressure.text = na
            binding.tvWind.text = na
        }
    }

    private fun updateUI(wm : CurrentWeather) {
        CoroutineScope(Main).launch {
            *//*Glide.with(this@HomeFragment)
                .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
                .centerCrop()
                .into(binding.ivWeather)*//*

            binding.tvTextTop.text = wm.temp + "°" + " | " + wm.wDescription

            binding.tvDot.text = "°"
            binding.tvTemp.text = wm.temp //+ "°"
            binding.tvMaxMinTemp.text = "Max.: ${wm.tempMax}°, min.: ${wm.tempMin}°"
            binding.tvWeather.text = wm.wDescription
            (activity as MainActivity).setToolbarTittle(SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(Date(wm.dt * 1000)))
            binding.tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunrise * 1000))
            binding.tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunset * 1000))

            //Feels like
            binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
            binding.tvTempFeelsDescription.text =
                if (wm.tempFeelsLike.toInt() == wm.temp.toInt()
                    || wm.tempFeelsLike.toInt() == wm.temp.toInt() - 1
                    || wm.tempFeelsLike.toInt() == wm.temp.toInt() + 1)
                    "Feels about the same"
                else if (wm.tempFeelsLike.toInt() <= wm.temp.toInt())
                    "Feels colder with the wind"
                else
                    ""

            // UV index
            binding.tvUV.text = if (wm.uvi < 1) "1" else wm.uvi.toInt().toString()
            binding.tvUVDescription.text =
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

            binding.vUVColor.backgroundTintList = if (wm.uvi.toInt() == 0)
                ResourcesCompat.getColorStateList(resources, R.color.green_A400, null)
            else if (wm.uvi.toInt() == 1 || wm.uvi.toInt() == 2)
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
            binding.tvVisibility.text = (wm.visibility / 1000).toString() + " km"
            binding.tvVisibilityDescription.text = if ((wm.visibility / 1000) >= 7 && (wm.visibility / 1000) <= 10)
                "Visibility reduced due to light haze"
            else if ((wm.visibility / 1000) >= 11 && (wm.visibility / 1000) <= 13)
                "Now it's clear"
            else if ((wm.visibility / 1000) >= 14)
                "Now it's quite clear"
            else ""

            //Humidity
            binding.tvHumidity.text = wm.humidity + "%"
            binding.tvDewPoint.text = "Dew point now: " + wm.dewPoint + "°"

            //Pressure
            binding.tvPressure.text = wm.pressure + " hPa"

            //Wind
            binding.tvWind.text = wm.windSpeed + " m/s"
            binding.tvWindDirection.text = "Direction: " + wm.windDeg + "°"
            binding.tvWindGust.text = "Gust: " + wm.windGust + " m/s"
        }
    }*/

   /* override fun onTabSelected(tab: TabLayout.Tab?) {
        updateIndicator(ENABLE)
        selectedTab = tab
        onRefresh()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }*/
}