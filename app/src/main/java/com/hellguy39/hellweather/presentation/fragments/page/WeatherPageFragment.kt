package com.hellguy39.hellweather.presentation.fragments.page

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.FragmentWeatherPageBinding

class WeatherPageFragment(private val weatherJsonObject: JsonObject): Fragment(R.layout.fragment_weather_page) {

    private lateinit var _binding: FragmentWeatherPageBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentWeatherPageBinding.bind(view)

        val current = weatherJsonObject.getAsJsonObject("current")
        _binding.tvTemp.text = current.get("temp").asDouble.toInt().toString()
    }
}