package com.hellguy39.hellweather.presentation.fragments.weather

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.adapter.DailyWeatherAdapter
import com.hellguy39.hellweather.presentation.adapter.HourlyWeatherAdapter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather) {

    private lateinit var binding : FragmentWeatherBinding
    private lateinit var viewModel: WeatherFragmentViewModel

    private val dailyWeatherList = mutableListOf<DailyWeather>()
    private val hourlyWeatherList = mutableListOf<HourlyWeather>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)

        binding.refreshLayout.setOnRefreshListener {
            checkPermissionAndFetchWeather()
        }

        binding.rvDailyWeather.apply {
            adapter = DailyWeatherAdapter(dailyWeatherList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.rvHourlyWeather.apply {
            adapter = HourlyWeatherAdapter(hourlyWeatherList)
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateUI(it)
            }
        }
        checkPermissionAndFetchWeather()
    }

    private fun updateUI(state: WeatherFragmentState) {
        binding.refreshLayout.isRefreshing = state.isLoading

        if (!state.error.isNullOrEmpty()) {
            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG)
                .setAction("Retry") {
                    checkPermissionAndFetchWeather()
                }
                .show()
        }

        state.oneCallWeather?.let {
            showData(it)
        }

    }

    private fun checkPermissionAndFetchWeather() {
        if((activity as MainActivity).checkPermissions()) {
            viewLifecycleOwner.lifecycleScope.launch(Dispatchers.IO) {
                val location = (activity as MainActivity).getCurrentLocation()
                withContext(Dispatchers.Main) {
                    if (location != null)
                        viewModel.fetchWeather(location)
                    else
                        Snackbar.make(binding.root, "Couldn't get geolocation", Snackbar.LENGTH_SHORT).show().also {
                            binding.refreshLayout.isRefreshing = false
                        }
                }
            }
        }
    }

    private fun updateHourlyWeatherRecycler(newItems: List<HourlyWeather>) {
        val adapter = binding.rvHourlyWeather.adapter

        // Clear old data
        val previousSize = adapter?.itemCount ?: 0
        hourlyWeatherList.clear()
        adapter?.notifyItemRangeRemoved(0, previousSize)

        // Set new data
        hourlyWeatherList.addAll(newItems)
        adapter?.notifyItemRangeInserted(0, newItems.size)
    }

    private fun updateDailyWeatherRecycler(newItems: List<DailyWeather>) {
        val adapter = binding.rvDailyWeather.adapter

        // Clear old data
        val previousSize = adapter?.itemCount ?: 0
        dailyWeatherList.clear()
        adapter?.notifyItemRangeRemoved(0, previousSize)

        // Set new data
        dailyWeatherList.addAll(newItems)
        adapter?.notifyItemRangeInserted(0, newItems.size)
    }

    private fun showData(data: OneCallWeather) {
        binding.tvTemp.text = data.currentWeather?.temp?.roundToInt().toString()

        data.dailyWeather?.let { updateDailyWeatherRecycler(it) }
        data.hourlyWeather?.let { updateHourlyWeatherRecycler(it) }
    }

    private fun navigateToLocationFragment() = findNavController()
        .navigate(WeatherFragmentDirections.actionWeatherFragmentToLocationFragment())
}