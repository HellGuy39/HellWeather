package com.hellguy39.hellweather.presentation.fragments.hourly_weather_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentHourlyWeatherDetailsBinding

class HourlyWeatherDetailsFragment : Fragment(R.layout.fragment_hourly_weather_details) {

    private lateinit var binding: FragmentHourlyWeatherDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentHourlyWeatherDetailsBinding.bind(view)
    }

}