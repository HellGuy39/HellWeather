package com.hellguy39.hellweather.presentation.fragments.weather

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.usecase.GetOneCallWeatherUseCase
import com.hellguy39.hellweather.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val getOneCallWeatherUseCase: GetOneCallWeatherUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(WeatherFragmentState())
    val uiState: StateFlow<WeatherFragmentState> = _uiState

    fun fetchWeather(location: Location) = viewModelScope.launch {
        getOneCallWeatherUseCase.invoke(
            fetchFromRemote = true,
            lat = location.latitude,
            lon = location.longitude
        ).collect {
            when(it) {
                is Resource.Success -> {
                    _uiState.value = WeatherFragmentState(
                        oneCallWeather = it.data as OneCallWeather
                    )
                }
                is Resource.Error -> {
                    _uiState.value = WeatherFragmentState(
                        oneCallWeather = it.data as OneCallWeather,
                        error = it.message
                    )
                }
                is Resource.Loading -> {
                    _uiState.value = WeatherFragmentState(
                        isLoading = it.isLoading
                    )
                }
            }
        }
    }
}