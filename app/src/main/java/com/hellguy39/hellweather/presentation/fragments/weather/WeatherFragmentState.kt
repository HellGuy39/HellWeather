package com.hellguy39.hellweather.presentation.fragments.weather

import com.hellguy39.hellweather.domain.model.WeatherForecast

data class WeatherFragmentState(
    val data: WeatherForecast? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
