package com.hellguy39.hellweather.presentation.fragments.daily_weather_details

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentDailyWeatherDetailsBinding

class DailyWeatherDetailsFragment : Fragment(R.layout.fragment_daily_weather_details) {

    private lateinit var binding: FragmentDailyWeatherDetailsBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentDailyWeatherDetailsBinding.bind(view)
    }

}