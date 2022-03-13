package com.hellguy39.hellweather.presentation.fragments.search

import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SearchFragmentBinding
import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.usecase.format.FormatUseCases
import com.hellguy39.hellweather.domain.utils.MM_HG
import com.hellguy39.hellweather.domain.utils.Unit
import com.hellguy39.hellweather.glide.GlideApp
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.State
import com.hellguy39.hellweather.utils.setToolbarNavigation
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    @Inject
    lateinit var formatUseCases: FormatUseCases

    private lateinit var _viewModel: SearchViewModel
    private lateinit var _binding: SearchFragmentBinding
    private lateinit var _fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _fragView = view
        _binding = SearchFragmentBinding.bind(view)
        _binding.toolbar.setToolbarNavigation(toolbar = _binding.toolbar, activity = activity as MainActivity)

        _binding.ibSearch.setOnClickListener {
            val input = _binding.etCity.text.toString()
            if (!checkInput(input)) {
                refreshing(true)
                _viewModel.fetchWeather(input)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        setObservers()
    }

    private fun setObservers() {
        _viewModel.getCurrentWeather().observe(viewLifecycleOwner) {
            if (isOnSearchFragment()) {
                if (it != null) {
                    refreshing(false)
                    updateUI(it)
                }
            }
        }
        _viewModel.getStatus().observe(viewLifecycleOwner) {
            when (it) {
                State.Error -> {
                    refreshing(false)
                    val errorMessage = _viewModel.getErrorMessage().value

                    if (!errorMessage.isNullOrEmpty()) {
                        _binding.collapseToolbar.title = errorMessage
                    }
                }
                State.Progress -> {
                    refreshing(true)
                }
            }
        }
    }

    private fun updateUI(currentWeather: CurrentWeather) {

        GlideApp.with(_binding.ivWeather.context)
            .load("https://openweathermap.org/img/wn/${currentWeather.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)

        val units = _viewModel.getUnits()

        val tempDesignation = when (units) {
            Unit.Standard.name -> resources.getString(R.string.kelvin)
            Unit.Metric.name -> resources.getString(R.string.celsius)
            Unit.Imperial.name -> resources.getString(R.string.fahrenheit)
            else -> resources.getString(R.string.degree)
        }

        _binding.tvSunrise.text = formatUseCases.formatTimeUseCase.invoke(currentWeather.sunrise)
        _binding.tvSunset.text = formatUseCases.formatTimeUseCase.invoke(currentWeather.sunset)

        _binding.tvDescription.text = currentWeather.wDescription
        _binding.tvTemp.text = currentWeather.temp
        _binding.tvDot.text = tempDesignation

        _binding.collapseToolbar.title = currentWeather.name
        _binding.tvHumidity.text = String.format(resources.getString(R.string.humidity_text), currentWeather.humidity)
        _binding.tvVisibility.text = String.format(resources.getString(R.string.visibility_text), (currentWeather.visibility / 1000))

        _binding.tvPressure.text = String.format(resources.getString(R.string.pressure_text),(currentWeather.pressure.toDouble() * MM_HG).toInt())

        _binding.tvWind.text = String.format(resources.getString(R.string.wind_speed_text),currentWeather.windSpeed)
        _binding.tvWindDirection.text = String.format(resources.getString(R.string.wind_direction_text),currentWeather.windDeg)
        _binding.tvWindGust.text = String.format(resources.getString(R.string.wind_gust_text),currentWeather.windGust)

        if (units == Unit.Standard.name)
            _binding.tvTempFeelsLike.text = String.format(resources.getString(R.string.temp_feels_like_kelvin_text),currentWeather.tempFeelsLike)
        else
            _binding.tvTempFeelsLike.text = String.format(resources.getString(R.string.temp_feels_like_degree_text),currentWeather.tempFeelsLike)

        _binding.tvTempFeelsDescription.text =
            if (currentWeather.tempFeelsLike.toInt() == currentWeather.temp.toInt()
                || currentWeather.tempFeelsLike.toInt() == currentWeather.temp.toInt() - 1
                || currentWeather.tempFeelsLike.toInt() == currentWeather.temp.toInt() + 1)
                resources.getString(R.string.feels_about_the_same)
            else
                ""

        fade()
    }

    private fun refreshing(b: Boolean) {
        if (b)
        {
            _binding.tfCity.isEnabled = false
            _binding.progressIndicator.visibility = View.VISIBLE
            _binding.ibSearch.isEnabled = false
        }
        else
        {
            _binding.tfCity.isEnabled = true
            _binding.progressIndicator.visibility = View.INVISIBLE
            _binding.ibSearch.isEnabled = true
        }
    }

    private fun fade() {
        _binding.nsvContent.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
        }
    }

    private fun isOnSearchFragment(): Boolean =
        findNavController().currentDestination?.id == R.id.searchFragment

    private fun checkInput(s : String) : Boolean = TextUtils.isEmpty(s)
}