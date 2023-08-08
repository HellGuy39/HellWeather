package com.hellguy39.hellweather.activity

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import org.koin.androidx.compose.koinViewModel

@Composable
fun WeatherScreen(
    weatherViewModel: WeatherViewModel = koinViewModel()
) {
    
    val uiState by weatherViewModel.uiState.collectAsStateWithLifecycle()
    val weatherState = uiState.weatherState
    
    Box(modifier = Modifier.fillMaxSize()) {
        when(weatherState) {
            is RealtimeWeatherState.Success -> {
                val currentWeather = weatherState.realtimeWeather.currentWeather
                val location = weatherState.realtimeWeather.location
                Column(
                    modifier = Modifier.fillMaxSize(),
                    verticalArrangement = Arrangement.spacedBy(
                        space = 8.dp,
                        alignment = Alignment.CenterVertically
                    ),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(text = location?.name.toString())
                    Text(text = currentWeather?.tempC.toString() + " °C")
                }
            }
            else -> Unit
        }
    }
}