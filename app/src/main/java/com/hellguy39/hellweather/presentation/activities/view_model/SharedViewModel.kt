package com.hellguy39.hellweather.presentation.activities.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellguy39.hellweather.domain.model.DailyWeather
import com.hellguy39.hellweather.domain.model.HourlyWeather

class SharedViewModel: ViewModel() {

    private val selectedDailyWeatherItem = MutableLiveData<DailyWeather>()
    private val selectedHourlyWeatherItem = MutableLiveData<HourlyWeather>()

    fun setDailyWeatherItem(dailyWeather: DailyWeather) {
        selectedDailyWeatherItem.value = dailyWeather
    }

    fun setHourlyWeatherItem(hourlyWeather: HourlyWeather) {
        selectedHourlyWeatherItem.value = hourlyWeather
    }

    fun getDailyWeatherItem(): LiveData<DailyWeather> = selectedDailyWeatherItem

    fun getHourlyWeatherItem(): LiveData<HourlyWeather> = selectedHourlyWeatherItem

}