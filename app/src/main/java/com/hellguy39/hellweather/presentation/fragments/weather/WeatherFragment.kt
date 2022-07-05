package com.hellguy39.hellweather.presentation.fragments.weather

import android.graphics.Color
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
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.platform.MaterialElevationScale
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherBinding
import com.hellguy39.hellweather.domain.model.*
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.view_model.SharedViewModel
import com.hellguy39.hellweather.presentation.adapter.*
import com.hellguy39.hellweather.utils.PermissionState
import com.hellguy39.hellweather.utils.formatAsDayWithTime
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather),
    DailyForecastAdapter.DailyWeatherItemCallback,
    HourlyForecastAdapter.HourlyWeatherItemCallback,
    View.OnClickListener
{
    private lateinit var binding : FragmentWeatherBinding

    private lateinit var viewModel: WeatherFragmentViewModel
    private val sharedViewModel by activityViewModels<SharedViewModel>()

    private val dailyWeatherList = mutableListOf<DailyWeather>()
    private val hourlyWeatherList = mutableListOf<HourlyWeather>()
    private val currentWeatherDetailsList = mutableListOf<DetailModel>()
    private val alertList = mutableListOf<Alert>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        viewModel = ViewModelProvider(this)[WeatherFragmentViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentWeatherBinding.bind(view)
        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.run {
            btnRefresh.setOnClickListener {
                checkPermissionAndFetchWeather()
            }
            refreshLayout.setOnRefreshListener {
                checkPermissionAndFetchWeather()
            }
            recipientCardScrim.setOnClickListener(this@WeatherFragment)
            chipCity.setOnClickListener(this@WeatherFragment)
        }

        setupRecyclers()
    }

    override fun onStart() {
        super.onStart()
        val state = viewModel.uiState.value
        if (!state.isLoading && state.data == null)
            checkPermissionAndFetchWeather()

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateUI(it)
            }
        }
    }

    private fun expandLocationChip() {
        val transform = MaterialContainerTransform().apply {
            startView = binding.chipCity
            endView = binding.cardLocation
            scrimColor = Color.TRANSPARENT
            endElevation = 16f
            addTarget(binding.cardLocation)
        }
        TransitionManager.beginDelayedTransition(binding.currentWeatherConstraintLayout, transform)
        binding.run {
            chipCity.visibility = View.INVISIBLE
            cardLocation.visibility = View.VISIBLE
            recipientCardScrim.visibility = View.VISIBLE
        }
    }

    private fun collapseLocationChip() {
        val transform = MaterialContainerTransform().apply {
            startView = binding.cardLocation
            endView = binding.chipCity
            scrimColor = Color.TRANSPARENT
            endElevation = 0f
            addTarget(binding.chipCity)
        }
        TransitionManager.beginDelayedTransition(binding.currentWeatherConstraintLayout, transform)
        binding.run {
            chipCity.visibility = View.VISIBLE
            cardLocation.visibility = View.INVISIBLE
            recipientCardScrim.visibility = View.GONE
        }
    }

    private fun setupRecyclers() {
        binding.run {
            rvDailyWeather.apply {
                adapter = DailyForecastAdapter(
                    dataSet = dailyWeatherList,
                    callback = this@WeatherFragment,
                    resources = resources
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            }
            rvHourlyWeather.apply {
                adapter = HourlyForecastAdapter(
                    dataSet = hourlyWeatherList,
                    callback = this@WeatherFragment,
                    resources = resources
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            rvCurrentWeatherDetails.apply {
                adapter = CurrentWeatherDetailsAdapter(
                    dataSet = currentWeatherDetailsList
                )
                layoutManager = GridLayoutManager(context, CurrentWeatherDetailsAdapter.SPAN_COUNT)
            }
            rvAlerts.apply {
                adapter = AlertsAdapter(
                    dataSet = alertList,
                    resources = resources
                )
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
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

        state.data?.let { data ->
            data.oneCallWeather?.let { oneCallWeather -> showWeatherData(oneCallWeather) }
        }

    }

    private fun checkPermissionAndFetchWeather() {
        if (!viewModel.uiState.value.isLoading) {
            when((activity as MainActivity).checkPermissions()) {
                PermissionState.Granted -> { viewModel.fetchWeather((activity as MainActivity).locationHelper) }
                PermissionState.Denied -> { navigateToLocationFragment() }
                PermissionState.GPSDisabled -> { navigateToLocationFragment() }
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

    private fun updateCurrentWeatherDetailsRecycler(currentWeather: CurrentWeather) {
        val adapter = binding.rvCurrentWeatherDetails.adapter

        // Clear old data
        val previousSize = adapter?.itemCount ?: 0
        currentWeatherDetailsList.clear()
        adapter?.notifyItemRangeRemoved(0, previousSize)

        // Set new data
        currentWeatherDetailsList.addAll(currentWeather.toDetailsModelList(resources))
        adapter?.notifyItemRangeInserted(0, currentWeatherDetailsList.size)
    }

    private fun updateAlertsRecycler(newItems: List<Alert>) {
        val adapter = binding.rvAlerts.adapter

        // Clear old data
        val previousSize = adapter?.itemCount ?: 0
        alertList.clear()
        adapter?.notifyItemRangeRemoved(0, previousSize)

        // Set new data
        alertList.addAll(newItems)
        adapter?.notifyItemRangeInserted(0, newItems.size)
    }

    private fun showWeatherData(data: OneCallWeather) {

        Glide.with(this)
            .load(IconHelper.getByIconId(data.currentWeather?.weather?.get(0)))
            .into(binding.ivIcon)

        TransitionManager.beginDelayedTransition(binding.refreshLayout, MaterialFadeThrough())

        binding.tvTemp.text = data.currentWeather?.temp?.roundToInt().toString()//resources.getString(R.string.value_as_temp, data.currentWeather?.temp?.roundToInt())
        binding.tvTempFeelsLike.text = resources.getString(
            R.string.feels_like,
            data.currentWeather?.feelsLike?.roundToInt()
        )
        binding.tvDate.text = data.currentWeather?.date?.formatAsDayWithTime()
        binding.tvWeatherDescription.text = data.currentWeather?.weather?.get(0)?.description?.replaceFirstChar(Char::titlecase)

//        if (data.lat != null || data.lon != null)
//            viewModel.fetchCity(data.lat!!, data.lon!!)

        // Recyclers
        data.dailyWeather?.let { updateDailyWeatherRecycler(it) }
        data.hourlyWeather?.let { updateHourlyWeatherRecycler(it) }
        data.currentWeather?.let { updateCurrentWeatherDetailsRecycler(it) }
        data.alerts?.let { updateAlertsRecycler(it) }
    }

    private fun navigateToLocationFragment() {
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
        reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
        val directions = WeatherFragmentDirections.actionWeatherFragmentToLocationFragment()
        findNavController().navigate(directions)
    }

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

    override fun onClick(view: View?) {
        when(view?.id) {
            binding.recipientCardScrim.id -> {
                if (binding.chipCity.visibility == View.INVISIBLE) {
                    collapseLocationChip()
                }
            }
            binding.chipCity.id -> {
                if(binding.chipCity.visibility == View.VISIBLE) {
                    expandLocationChip()
                }
            }
            else -> Unit
        }
    }
}