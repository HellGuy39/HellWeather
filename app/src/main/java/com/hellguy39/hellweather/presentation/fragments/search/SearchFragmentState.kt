package com.hellguy39.hellweather.presentation.fragments.search

import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.domain.model.WeatherForecast

data class SearchFragmentState(
    val data: List<LocationInfo>? = null,
    val cachedData: List<LocationInfo>? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
