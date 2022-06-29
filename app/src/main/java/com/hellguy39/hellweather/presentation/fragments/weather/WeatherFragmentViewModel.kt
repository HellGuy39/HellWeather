package com.hellguy39.hellweather.presentation.fragments.weather

import android.util.Log
import androidx.lifecycle.*
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.usecase.GetOneCallWeatherUseCase
import com.hellguy39.hellweather.domain.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val getOneCallWeatherUseCase: GetOneCallWeatherUseCase
): ViewModel() {

    private val _uiState = MutableStateFlow(WeatherFragmentState())
    val uiState: StateFlow<WeatherFragmentState> = _uiState

    fun fetchWeather() = viewModelScope.launch {
        getOneCallWeatherUseCase.invoke(true, 45.0, 38.0).collect {
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