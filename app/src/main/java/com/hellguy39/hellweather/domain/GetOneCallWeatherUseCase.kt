package com.hellguy39.hellweather.domain

import com.hellguy39.hellweather.data.api.ApiRepository
import com.hellguy39.hellweather.domain.models.WeatherData
import com.hellguy39.hellweather.utils.Converter
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.SUCCESSFUL

class GetOneCallWeatherUseCase {
    suspend fun execute(apiRepository: ApiRepository, model: OneCallResponse) : WeatherData {
        val weatherData = WeatherData()

        val response = apiRepository.getWeatherOneCall(
            model.lat,
            model.lon,
            model.exclude,
            model.units,
            model.lang,
            model.appId
        )

        if (response.isSuccessful) {
            val jsonObject = response.body()
            if (jsonObject != null)
            {
                val converter = Converter()
                val _weatherData = converter.toWeatherObject(jsonObject)
                weatherData.requestResult = SUCCESSFUL
                weatherData.currentWeather = _weatherData.currentWeather
                weatherData.hourlyWeather = _weatherData.hourlyWeather
                weatherData.dailyWeather = _weatherData.dailyWeather
            }
            else
            {
                weatherData.requestResult = ERROR
                weatherData.errorBody = "Response body is null"
            }
        }
        else
        {
            weatherData.requestResult = ERROR
            weatherData.errorBody = response.errorBody().toString()
        }

        return weatherData
    }
}