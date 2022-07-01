package com.hellguy39.hellweather.presentation.fragments.hourly_weather_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHourlyWeatherDetailsBinding
import com.hellguy39.hellweather.domain.model.HourlyWeather
import com.hellguy39.hellweather.presentation.activities.main.SharedViewModel
import com.hellguy39.hellweather.utils.getColorFromAttr

class HourlyWeatherDetailsFragment : Fragment(R.layout.fragment_hourly_weather_details) {

    private lateinit var binding: FragmentHourlyWeatherDetailsBinding

    private val sharedViewModel by activityViewModels<SharedViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host_fragment
            scrimColor = Color.TRANSPARENT
            setAllContainerColors(getColorFromAttr(R.attr.colorSurface))
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHourlyWeatherDetailsBinding.bind(view)
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.getHourlyWeatherItem().observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(hourlyWeather: HourlyWeather) {
        binding.tvContent.text = hourlyWeather.toString()
    }
}