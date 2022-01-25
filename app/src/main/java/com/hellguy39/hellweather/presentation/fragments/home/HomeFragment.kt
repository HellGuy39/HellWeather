package com.hellguy39.hellweather.presentation.fragments.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.broooapps.graphview.CurveGraphConfig
import com.broooapps.graphview.models.GraphData
import com.broooapps.graphview.models.PointMap
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHomeBinding
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.NextDaysAdapter
import com.hellguy39.hellweather.presentation.adapter.NextHoursAdapter
import com.hellguy39.hellweather.repository.database.pojo.CurrentWeather
import com.hellguy39.hellweather.repository.database.pojo.DailyWeather
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Dispatchers.Main
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class HomeFragment : Fragment(R.layout.fragment_home), TabLayout.OnTabSelectedListener {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: HomeViewModel
    private var selectedTab: TabLayout.Tab? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.loading))
        (activity as MainActivity).updateToolbarMenu(ENABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHomeBinding.bind(view)

        confGraph()
        binding.tabLayout.addOnTabSelectedListener(this)
        setObservers()

    }

    override fun onStart() {
        super.onStart()

        /*if (viewModel.isUpdate.value == false || viewModel.isUpdate.value == null)
            onRefresh()*/
    }

    fun onRefresh() {

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
    }

    private fun setObservers() {

        viewModel.userLocationsLive.observe(this,{
            for(n in it.indices) {
                val tab = binding.tabLayout.newTab()
                tab.text = it[n].locationName
                tab.tag = it[n].id
                /*if (n == 0) {
                    tab.icon = (ResourcesCompat.getDrawable(resources, R.drawable.ic_round_home_24, null))
                }*/
                binding.tabLayout.addTab(tab)
            }
        })

        viewModel.isUpdate.observe(this, {
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
            /*Glide.with(this@HomeFragment)
                .load("https://openweathermap.org/img/wn/${wm.icon}@2x.png")
                .centerCrop()
                .into(binding.ivWeather)*/

            binding.tvTextTop.text = wm.temp + "°" + " | " + wm.wDescription

            binding.tvDot.text = "°"
            binding.tvTemp.text = wm.temp //+ "°"
            binding.tvMaxMinTemp.text = "Max.: ${wm.tempMax}°, min.: ${wm.tempMin}°"
            binding.tvWeather.text = wm.wDescription
            (activity as MainActivity).setToolbarTittle(SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(Date(wm.dt * 1000)))
            binding.tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunrise * 1000))
            binding.tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(wm.sunset * 1000))
            binding.tvTempFeelsLike.text = wm.tempFeelsLike + "°"
            binding.tvHumidity.text = wm.humidity + "%"
            binding.tvPressure.text = wm.pressure + " hPa"
            binding.tvWind.text = wm.windSpeed + " m/s"
        }
    }

    override fun onTabSelected(tab: TabLayout.Tab?) {
        updateIndicator(ENABLE)
        selectedTab = tab
        onRefresh()
    }

    override fun onTabUnselected(tab: TabLayout.Tab?) {

    }

    override fun onTabReselected(tab: TabLayout.Tab?) {

    }
}