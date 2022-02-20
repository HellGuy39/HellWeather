package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.text.TextUtils
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.SearchFragmentBinding
import com.hellguy39.hellweather.glide.GlideApp
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.domain.usecase.prefs.GetUnitsUseCase
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.AndroidEntryPoint
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class SearchFragment: Fragment(R.layout.search_fragment) {

    private lateinit var _viewModel: SearchViewModel
    private lateinit var _binding: SearchFragmentBinding
    private lateinit var _fragView: View

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewModel = ViewModelProvider(this)[SearchViewModel::class.java]

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        (activity as MainActivity).setToolbarTittle(getString(R.string.tittle_search))
        (activity as MainActivity).updateToolbarMenu(DISABLE)

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _fragView = view
        _binding = SearchFragmentBinding.bind(view)

        _binding.ibSearch.setOnClickListener {
            val input = _binding.etCity.text.toString()
            if (!checkInput(input)) {
                refreshing(true)
                _viewModel.getCurrentWeather(input)
            }
            else
            {
                //_fragView.shortSnackBar(resources.getString(R.string.not_funny))
                //_binding.tfCity.error = resources.getString(R.string.not_funny)
            }
        }
    }

    override fun onStart() {
        super.onStart()
        _viewModel.currentWeatherLive.observe(viewLifecycleOwner) {
            if (findNavController().currentDestination?.id == R.id.searchFragment)
            {
                if (it != null)
                {
                    refreshing(false)
                    updateUI(it)
                }
            }
        }
        _viewModel.statusLive.observe(viewLifecycleOwner) {
            when (it) {
                FAILURE -> {
                    refreshing(false)
                    if (findNavController().currentDestination?.id == R.id.searchFragment)
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.connection_lost))
                }
                ERROR -> {
                    refreshing(false)
                    if (findNavController().currentDestination?.id == R.id.searchFragment)
                        (activity as MainActivity).setToolbarTittle(resources.getString(R.string.error))
                }
                IN_PROGRESS -> {
                    refreshing(true)
                }
            }
        }
    }

    private fun updateUI(currentWeather: CurrentWeather) {

        if (findNavController().currentDestination?.id == R.id.searchFragment) {
            (activity as MainActivity).setToolbarTittle(
                SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(
                    currentWeather.dt * 1000)
            )
        }

        GlideApp.with(_binding.ivWeather.context)
            .load("https://openweathermap.org/img/wn/${currentWeather.icon}@2x.png")
            .centerCrop()
            .into(_binding.ivWeather)

        val units = _viewModel.getUnits()

        val tempDesignation = when (units) {
            STANDARD -> resources.getString(R.string.kelvin)
            METRIC -> resources.getString(R.string.celsius)
            IMPERIAL -> resources.getString(R.string.fahrenheit)
            else -> resources.getString(R.string.degree)
        }

        _binding.tvSunrise.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(currentWeather.sunrise * 1000))
        _binding.tvSunset.text = SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(currentWeather.sunset * 1000))

        _binding.tvDescription.text = currentWeather.wDescription
        _binding.tvTemp.text = currentWeather.temp
        _binding.tvDot.text = tempDesignation

        _binding.tvName.text = currentWeather.name
        _binding.tvHumidity.text = String.format(resources.getString(R.string.humidity_text), currentWeather.humidity)
        _binding.tvVisibility.text = String.format(resources.getString(R.string.visibility_text), (currentWeather.visibility / 1000))

        //Pressure
        _binding.tvPressure.text = String.format(resources.getString(R.string.pressure_text),(currentWeather.pressure.toDouble() * MM_HG).toInt())

        //Wind
        _binding.tvWind.text = String.format(resources.getString(R.string.wind_speed_text),currentWeather.windSpeed)
        _binding.tvWindDirection.text = String.format(resources.getString(R.string.wind_direction_text),currentWeather.windDeg)
        _binding.tvWindGust.text = String.format(resources.getString(R.string.wind_gust_text),currentWeather.windGust)

        if (units == STANDARD)
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

        crossfade()
    }

    private fun refreshing(b: Boolean) {
        if (b)
        {
            _binding.tfCity.isEnabled = false
            _binding.progressIndicator.visibility = View.VISIBLE
            _binding.ibSearch.isEnabled = false
            (activity as MainActivity).setToolbarTittle(resources.getString(R.string.searching))
        }
        else
        {
            _binding.tfCity.isEnabled = true
            _binding.progressIndicator.visibility = View.INVISIBLE
            _binding.ibSearch.isEnabled = true
        }
    }

    private fun crossfade() {
        _binding.nsvContent.apply {
            alpha = 0f
            visibility = View.VISIBLE

            animate()
                .alpha(1f)
                .setDuration(300)
                .setListener(null)
        }
    }

    private fun checkInput(s : String) : Boolean = TextUtils.isEmpty(s)
}