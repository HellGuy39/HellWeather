package com.hellguy39.hellweather.presentation.fragments.weather

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.util.Resource
import com.hellguy39.hellweather.domain.wrapper.RemoteDataUseCases
import com.hellguy39.hellweather.helpers.LocationHelper
import com.hellguy39.hellweather.utils.update
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WeatherFragmentViewModel @Inject constructor(
    private val remoteDataUseCases: RemoteDataUseCases
): ViewModel() {

    private val _uiState = MutableStateFlow(WeatherFragmentState())
    val uiState: StateFlow<WeatherFragmentState> = _uiState

    fun fetchLocationAndWeather(locationHelper: LocationHelper) = viewModelScope.launch {
//        _uiState.value = WeatherFragmentState(
//            isLoading = true
//        )
        val location = locationHelper.getLocation()

    }

    fun fetchWeather(locationHelper: LocationHelper) = viewModelScope.launch {
        _uiState.value = WeatherFragmentState(
            isLoading = true
        )
        val location = locationHelper.getLocation()

        if (location == null) {
            _uiState.value = WeatherFragmentState(
                data = null,
                error = "Couldn't get geolocation"
            )
        } else {
            remoteDataUseCases.getOneCallWeather.invoke(
                fetchFromRemote = true,
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

    fun fetchCity(lat: Double, lon: Double) = viewModelScope.launch {
        remoteDataUseCases.getLocationName.invoke(lat = lat, lon = lon, limit = 5).collect {
            when (it) {
                is Resource.Success -> {
                    Log.d("SUCCESS", it.data.toString())
                }
                is Resource.Error -> {
                    Log.d("ERROR", it.message.toString())
                }
                is Resource.Loading -> {
                    Log.d("LOADING", it.message.toString())
                }
            }
        }
    }
}
