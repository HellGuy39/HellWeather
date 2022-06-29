package com.hellguy39.hellweather.presentation.fragments.weather

import com.hellguy39.hellweather.domain.model.OneCallWeather

data class WeatherFragmentState(
    val oneCallWeather: OneCallWeather? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
