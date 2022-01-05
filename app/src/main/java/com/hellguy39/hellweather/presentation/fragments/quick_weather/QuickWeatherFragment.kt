package com.hellguy39.hellweather.presentation.fragments.quick_weather

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.QuickWeatherFragmentBinding

class QuickWeatherFragment : Fragment(R.layout.quick_weather_fragment) {

    private lateinit var viewModel: QuickWeatherViewModel
    private lateinit var binding: QuickWeatherFragmentBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = QuickWeatherFragmentBinding.bind(view)
        viewModel = ViewModelProvider(this)[QuickWeatherViewModel::class.java]



    }

}