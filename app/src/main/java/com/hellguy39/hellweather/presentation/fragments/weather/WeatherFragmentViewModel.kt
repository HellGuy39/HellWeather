package com.hellguy39.hellweather.presentation.fragments.weather

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.GetCachedForecastUseCase
import com.hellguy39.hellweather.domain.usecase.GetWeatherForecastUseCase
import com.hellguy39.hellweather.domain.util.Resource
import com.hellguy39.hellweather.location.LocationManager
import com.hellguy39.hellweather.utils.PermissionState
import com.hellguy39.hellweather.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val getWeatherForecastUseCase: GetWeatherForecastUseCase,
    private val getCachedForecastUseCase: GetCachedForecastUseCase,
    private val locationManager: LocationManager
): ViewModel() {

    private val _uiState = MutableStateFlow(WeatherFragmentState())
    val uiState: StateFlow<WeatherFragmentState> = _uiState

    init {
        fetchWeatherFromLocal()

        if (locationManager.checkPermissions() == PermissionState.Granted)
            fetchWeatherFromRemote()
    }

    private fun fetchWeatherFromLocal() = viewModelScope.launch {
        getCachedForecastUseCase.invoke().collect { resource ->
            when (resource) {
                is Resource.Success -> {
                    _uiState.update { state ->
                        state.copy(
                            data = resource.data
                        )
                    }
                }
                else -> Unit
            }
        }
    }

    fun fetchWeatherFromRemote() = viewModelScope.launch {

        _uiState.value = WeatherFragmentState(
            isLoading = true
        )

        val location = locationManager.getLocation()

        if (location == null) {
            _uiState.value = WeatherFragmentState(
                data = null,
                error = "Couldn't get geolocation"
            )
        } else {
            getWeatherForecastUseCase.invoke(
                lat = location.latitude,
                lon = location.longitude
            ).collect { resource ->
                when (resource) {
                    is Resource.Success -> {
                        _uiState.update { state ->
                            state.copy(
                                data = resource.data
                            )
                        }
                    }
                    is Resource.Error -> {
                        _uiState.update { state ->
                            state.copy(
                                error = resource.message
                            )
                        }
                    }
                    is Resource.Loading -> {
                        _uiState.update { state ->
                            state.copy(
                                isLoading = resource.isLoading
                            )
                        }
                    }
                }
            }
        }
    }
}
