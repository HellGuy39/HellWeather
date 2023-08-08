package com.hellguy39.hellweather.activity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.core.domain.repository.WeatherRepository
import com.hellguy39.hellweather.core.model.RealtimeWeather
import com.hellguy39.hellweather.core.model.RequestQuery
import com.hellguy39.hellweather.core.model.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class WeatherViewModel(
    private val weatherRepository: WeatherRepository
): ViewModel() {

    private val weatherState: MutableStateFlow<RealtimeWeatherState> = MutableStateFlow(RealtimeWeatherState.Idle)

    val uiState = weatherState
        .map { WeatherUiState(weatherState = it) }
        .stateIn(
            initialValue = WeatherUiState(),
            started = SharingStarted.WhileSubscribed(5_000),
            scope = viewModelScope
        )

    init {
        fetchWeather()
    }

    private fun fetchWeather() {
        val flow = weatherRepository.getRealtimeWeather(RequestQuery.City("Krasnodar"))
        viewModelScope.launch {
            flow.collect { resource ->
                when(resource) {
                    is Resource.Success -> {
                        weatherState.update { RealtimeWeatherState.Success(resource.data) }
                    }
                    is Resource.Error -> Unit
                    is Resource.Loading -> {
                        if (resource.isLoading) {
                            weatherState.update { RealtimeWeatherState.Loading }
                        }
                    }
                }
            }
        }
    }

}

data class WeatherUiState(
    val weatherState: RealtimeWeatherState = RealtimeWeatherState.Idle
)

sealed class RealtimeWeatherState {

    data class Success(val realtimeWeather: RealtimeWeather): RealtimeWeatherState()

    data object Loading: RealtimeWeatherState()

    data object Idle: RealtimeWeatherState()

}