package com.hellguy39.hellweather.domain.usecase.requests.weather

import com.hellguy39.hellweather.domain.models.WeatherData
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.request_models.OneCallRequest
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter

class GetOneCallWeatherUseCase(private val apiRepositoryImpl: ApiRepository) {

    suspend operator fun invoke(model: OneCallRequest): Resource<WeatherData> {

        val response = apiRepositoryImpl.getWeatherOneCall(
            lat = model.lat,
            lon = model.lon,
            exclude = model.exclude,
            units = model.units,
            lang = model.lang,
            appId = model.appId
        )

        if (response.isSuccessful) {
            val jsonObject = response.body()
            if (jsonObject != null)
            {
                val _weatherData = converter().toWeatherObject(jsonObject)
                return Resource.Success(
                    WeatherData(
                        _weatherData.currentWeather,
                        _weatherData.hourlyWeather,
                        _weatherData.dailyWeather
                    )
                )
            }
            else
            {
                return Resource.Error("Response body is null")
            }
        }
        else
        {
            return Resource.Error(response.errorBody().toString())
        }
    }
}