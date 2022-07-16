package com.hellguy39.hellweather.presentation.fragments.weather

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.core.view.doOnPreDraw
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.TransitionManager
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.transition.MaterialFadeThrough
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherBinding
import com.hellguy39.hellweather.domain.model.*
import com.hellguy39.hellweather.format.DateFormatter
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.location.LocationManager
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.presentation.activities.main.PermissionCallback
import com.hellguy39.hellweather.presentation.activities.main.SharedViewModel
import com.hellguy39.hellweather.presentation.adapter.*
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
import kotlin.math.roundToInt

@AndroidEntryPoint
class WeatherFragment : Fragment(R.layout.fragment_weather),
    DailyForecastAdapter.DailyWeatherItemCallback,
    HourlyForecastAdapter.HourlyWeatherItemCallback,
    View.OnClickListener, PermissionCallback
{
    private lateinit var binding : FragmentWeatherBinding

    @Inject lateinit var locationManager: LocationManager

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
            //btnRefresh.setOnClickListener(this@WeatherFragment)
            toolbar.setOnMenuItemClickListener {  item ->
                when (item.itemId) {
                    R.id.share -> {
                        viewModel.uiState.value.data?.let { shareWeather(it) }
                        true
                    }
                    R.id.refresh -> {
                        checkPermissionAndFetchWeather()
                        true
                    }
                    else -> false
                }
            }
            toolbar.setNavigationOnClickListener {
                (activity as MainActivity).openDrawer()
            }
//            refreshLayout.setOnRefreshListener {
//                checkPermissionAndFetchWeather()
//            }
            recipientCardScrim.setOnClickListener(this@WeatherFragment)
            chipCity.setOnClickListener(this@WeatherFragment)
        }

        setupRecyclers()
    }

    override fun onStart() {
        super.onStart()
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect {
                updateUI(it)
            }
        }
    }

    private fun expandLocationChip() {
        TransitionManager.beginDelayedTransition(
            binding.currentWeatherConstraintLayout,
            binding.chipCity.transformTo(binding.cardLocation, 16f)
        )
        binding.run {
            chipCity.visibility = View.INVISIBLE
            cardLocation.visibility = View.VISIBLE
            recipientCardScrim.visibility = View.VISIBLE
        }
    }

    private fun collapseLocationChip() {
        TransitionManager.beginDelayedTransition(
            binding.currentWeatherConstraintLayout,
            binding.cardLocation.transformTo(binding.chipCity, 0f)
        )
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
                adapter = DetailsAdapter(
                    dataSet = currentWeatherDetailsList,
                    resources = resources
                )
                layoutManager = GridLayoutManager(context, DetailsAdapter.SPAN_COUNT)
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
        //binding.refreshLayout.isRefreshing = state.isLoading

        if (!state.error.isNullOrEmpty()) {
            Snackbar.make(binding.root, state.error, Snackbar.LENGTH_LONG)
                .setAction(R.string.text_retry) {
                    checkPermissionAndFetchWeather()
                }.show()
        }

        state.data?.let { data ->
            data.oneCallWeather?.let { oneCallWeather -> showWeatherData(oneCallWeather) }
            data.locationInfo?.let { list -> if (list.isNotEmpty()) showLocationInfo(list[0]) }
        }
    }

    private fun checkPermissionAndFetchWeather() {
        if (!viewModel.uiState.value.isLoading) {
            when(locationManager.checkPermissions()) {
                PermissionState.Granted -> {
                    viewModel.fetchWeatherFromRemote()
                }
                PermissionState.Denied -> {
                    (activity as MainActivity).requestPermission(this)
                }
                PermissionState.GPSDisabled -> {
                    Snackbar.make(
                        binding.root,
                        "Enable geolocation, please",
                        Snackbar.LENGTH_SHORT
                    ).setAction("Settings") {
                        (activity as MainActivity).openLocationSettings()
                    }.show()
                }
            }
        }
    }

    private fun showWeatherData(data: OneCallWeather) {

        TransitionManager.beginDelayedTransition(binding.refreshLayout, MaterialFadeThrough())

        binding.run {
            toolbar.title = DateFormatter.format(data.currentWeather?.date, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)
            tvTemp.text = data.currentWeather?.temp?.roundToInt().toString()
            tvTempFeelsLike.text = resources.getString(
                R.string.text_temp_feels_like,
                data.currentWeather?.feelsLike?.roundToInt()
            )
            tvWeatherDescription.text = data.currentWeather?.weather?.get(0)?.description?.replaceFirstChar(Char::titlecase)
            ivIcon.setImageAsync(IconHelper.getByIconId(data.currentWeather?.weather?.get(0)))
            data.dailyWeather?.let { rvDailyWeather.updateAndClearRecycler(dailyWeatherList, it) }
            data.hourlyWeather?.let { rvHourlyWeather.updateAndClearRecycler(hourlyWeatherList, it) }
            data.currentWeather?.let { rvCurrentWeatherDetails.updateAndClearRecycler(currentWeatherDetailsList, it.toDetailsModelList(resources)) }
            data.alerts?.let { rvAlerts.updateAndClearRecycler(alertList, it) }
        }
    }

    private fun showLocationInfo(info: LocationInfo) {
        binding.run {
            chipCity.text = getString(R.string.text_country_and_city_name, info.country, info.name)
            tvLat.text = getString(R.string.text_lat, info.lat?.toFloat())
            tvLon.text = getString(R.string.text_lon, info.lon?.toFloat())
            tvCountry.text = getString(R.string.text_country, info.country)
            tvState.text = getString(R.string.text_state, info.state)
            tvName.text = getString(R.string.text_city_name, info.name)
        }
    }

    private fun shareWeather(forecast: WeatherForecast) {
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(
                Intent.EXTRA_TEXT,
                "Weather in ${forecast.locationInfo?.get(0)?.country}, ${forecast.locationInfo?.get(0)?.name} " +
                        "on ${DateFormatter.format(forecast.oneCallWeather?.currentWeather?.date, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)}." +
                        "Temp: ${resources.getString(R.string.text_value_temp, forecast.oneCallWeather?.currentWeather?.temp?.roundToInt())}, " +
                        "feels like: ${resources.getString(R.string.text_value_temp, forecast.oneCallWeather?.currentWeather?.feelsLike?.roundToInt())}, " +
                        "description: ${forecast.oneCallWeather?.currentWeather?.weather?.get(0)?.description}, " +
                        "wind: ${resources.getString(R.string.text_value_meters_per_sec, forecast.oneCallWeather?.currentWeather?.windSpeed?.roundToInt())}, " +
                        "humidity: ${resources.getString(R.string.text_value_percents, forecast.oneCallWeather?.currentWeather?.humidity)}"
            )
            type = "text/plain"
        }
        startActivity(Intent.createChooser(intent,"Share to:"))
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
//            binding.btnRefresh.id -> {
//                checkPermissionAndFetchWeather()
//            }
            binding.recipientCardScrim.id -> {
                if (!binding.chipCity.isVisible)
                    collapseLocationChip()
            }
            binding.chipCity.id -> {
                if(binding.chipCity.isVisible)
                    expandLocationChip()
            }
            else -> Unit
        }
    }

    override fun onPermissionGranted() {
        checkPermissionAndFetchWeather()
    }

    override fun onPermissionDenied() {
        Snackbar.make(
            binding.root,
            "Permission denied",
            Snackbar.LENGTH_SHORT
        ).setAction("Retry") {
            (activity as MainActivity).requestPermission(this)
        }.show()
    }
}