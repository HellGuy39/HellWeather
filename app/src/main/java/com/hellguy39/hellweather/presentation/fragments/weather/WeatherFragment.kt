package com.hellguy39.hellweather.presentation.fragments.weather

import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.platform.MaterialElevationScale
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherBinding
import com.hellguy39.hellweather.domain.model.CurrentWeather
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.presentation.activities.main.IconHelper
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.SharedViewModel
import com.hellguy39.hellweather.presentation.adapter.CurrentWeatherDetailsAdapter
import com.hellguy39.hellweather.presentation.adapter.DailyWeatherAdapter
import com.hellguy39.hellweather.presentation.adapter.HourlyWeatherAdapter
import com.hellguy39.hellweather.utils.Detail
import com.hellguy39.hellweather.utils.formatAsDayWithTime
import com.hellguy39.hellweather.utils.formatAsHour
import com.hellguy39.hellweather.utils.toKilometers
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather),
    DailyWeatherAdapter.DailyWeatherItemCallback,
    HourlyWeatherAdapter.HourlyWeatherItemCallback
{
    private lateinit var binding : FragmentWeatherBinding

    private lateinit var viewModel: WeatherFragmentViewModel
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val dailyWeatherList = mutableListOf<DailyWeather>()
    private val hourlyWeatherList = mutableListOf<HourlyWeather>()
    private val currentWeatherDetailsList = mutableListOf<CurrentWeatherDetailsAdapter.DetailModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.refreshLayout.setOnRefreshListener {
            checkPermissionAndFetchWeather()
        }

        binding.rvDailyWeather.apply {
            adapter = DailyWeatherAdapter(
                dataSet = dailyWeatherList,
                callback = this@WeatherFragment,
                resources = resources
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        }
        binding.rvHourlyWeather.apply {
            adapter = HourlyWeatherAdapter(
                dataSet = hourlyWeatherList,
                callback = this@WeatherFragment,
                resources = resources
            )
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        }
        binding.rvCurrentWeatherDetails.apply {
            adapter = CurrentWeatherDetailsAdapter(
                dataSet = currentWeatherDetailsList
            )
            layoutManager = GridLayoutManager(context, CurrentWeatherDetailsAdapter.SPAN_COUNT)
        }
    }

    override fun onStart() {
        super.onStart()
        val state = viewModel.uiState.value
        if (!state.isLoading && state.oneCallWeather == null)
            checkPermissionAndFetchWeather()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateUI(it)
            }
        }
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
            viewModel.fetchWeather((activity as MainActivity).locationHelper)
        } else {
            binding.refreshLayout.isRefreshing = false
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

    private fun updateCurrentWeatherDetailsRecycler(currentWeather: CurrentWeather) {
        val adapter = binding.rvCurrentWeatherDetails.adapter

        // Clear old data
        val previousSize = adapter?.itemCount ?: 0
        currentWeatherDetailsList.clear()
        adapter?.notifyItemRangeRemoved(0, previousSize)

        // Set new data
        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.Sunrise,
            currentWeather.sunrise?.formatAsHour())
        )

        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.Humidity,
            resources.getString(R.string.value_in_percents, currentWeather.humidity))
        )

        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.Pressure,
            currentWeather.pressure.toString())
        )

        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.Sunset,
            currentWeather.sunset?.formatAsHour())
        )

        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.UVI,
            currentWeather.uvi?.roundToInt().toString())
        )

        currentWeatherDetailsList.add(CurrentWeatherDetailsAdapter.DetailModel(
            Detail.Visibility,
            currentWeather.visibility.toKilometers())
        )

        adapter?.notifyItemRangeInserted(0, currentWeatherDetailsList.size)
    }

    private fun showData(data: OneCallWeather) {
        TransitionManager.beginDelayedTransition(binding.refreshLayout, MaterialFadeThrough())

        binding.tvTemp.text = resources.getString(R.string.value_as_temp, data.currentWeather?.temp?.roundToInt())
        binding.tvTempFeelsLike.text = resources.getString(
            R.string.feels_like,
            data.currentWeather?.feelsLike?.roundToInt()
        )
        binding.tvDate.text = data.currentWeather?.date?.formatAsDayWithTime()
        binding.tvWeatherDescription.text = data.currentWeather?.weather?.get(0)?.description?.replaceFirstChar(Char::titlecase)

        Glide.with(this)
            .load(IconHelper.getByIconId(data.currentWeather?.weather?.get(0)))
            .into(binding.ivIcon)

        // Recyclers
        data.dailyWeather?.let { updateDailyWeatherRecycler(it) }
        data.hourlyWeather?.let { updateHourlyWeatherRecycler(it) }
        data.currentWeather?.let { updateCurrentWeatherDetailsRecycler(it) }
    }

//    private fun navigateToLocationFragment() = findNavController()
//        .navigate(WeatherFragmentDirections.actionWeatherFragmentToLocationFragment())

    private fun navigateToDailyWeatherDetailsFragment(itemView: View) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        val directions = WeatherFragmentDirections.actionWeatherFragmentToDailyWeatherDetailsFragment()
        val extras = FragmentNavigatorExtras(itemView to getString(R.string.weather_card_details_transition))
        findNavController().navigate(directions, extras)
    }

    private fun navigateToHourlyWeatherDetailsFragment(itemView: View) {
        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
        val directions = WeatherFragmentDirections.actionWeatherFragmentToHourlyWeatherDetailsFragment()
        val extras = FragmentNavigatorExtras(itemView to getString(R.string.weather_card_details_transition))
        findNavController().navigate(directions, extras)
    }

    override fun onClick(dailyWeather: DailyWeather, position: Int, itemView: View) {
        sharedViewModel.setDailyWeatherItem(dailyWeather)
        navigateToDailyWeatherDetailsFragment(itemView)
    }

    override fun onClick(hourlyWeather: HourlyWeather, position: Int, itemView: View) {
        sharedViewModel.setHourlyWeatherItem(hourlyWeather)
        navigateToHourlyWeatherDetailsFragment(itemView)
    }
}