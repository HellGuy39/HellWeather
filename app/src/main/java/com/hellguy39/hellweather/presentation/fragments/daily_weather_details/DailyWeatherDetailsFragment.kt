package com.hellguy39.hellweather.presentation.fragments.daily_weather_details

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.platform.MaterialContainerTransform
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentDailyWeatherDetailsBinding
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.presentation.activities.view_model.SharedViewModel
import com.hellguy39.hellweather.utils.getColorFromAttr

class DailyWeatherDetailsFragment : Fragment(R.layout.fragment_daily_weather_details) {

    private lateinit var binding: FragmentDailyWeatherDetailsBinding

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
        binding = FragmentDailyWeatherDetailsBinding.bind(view)
        binding.btnClose.setOnClickListener {
            findNavController().popBackStack()
        }
    }

    override fun onStart() {
        super.onStart()
        sharedViewModel.getDailyWeatherItem().observe(viewLifecycleOwner) {
            updateUI(it)
        }
    }

    private fun updateUI(dailyWeather: DailyWeather) {
        binding.tvContent.text = dailyWeather.toString()
    }
}