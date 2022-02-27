package com.hellguy39.hellweather.domain.usecase.requests.weather

import com.google.gson.JsonObject
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.models.request.OneCallRequest
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter
import retrofit2.Response

class GetOneCallWeatherUseCase(private val apiRepositoryImpl: ApiRepository) {

    suspend operator fun invoke(model: OneCallRequest): Resource<WeatherData> {

        val response: Response<JsonObject>

        try {
            response = apiRepositoryImpl.getWeatherOneCall(
                lat = model.lat,
                lon = model.lon,
                exclude = model.exclude,
                units = model.units,
                lang = model.lang,
                appId = model.appId
            )
        } catch (e: Exception) {
            return Resource.Error(message = "No internet connection")
        }

        if (response.isSuccessful) {
            val jsonObject = response.body()

            return if (jsonObject != null) {
                val weatherData = converter().toWeatherObject(jsonObject)
                Resource.Success(data =
                    WeatherData(
                        weatherData.currentWeather,
                        weatherData.hourlyWeather,
                        weatherData.dailyWeather
                    )
                )
            } else {
                Resource.Error(message = "Response body is null")
            }
        }
        else
        {
            return Resource.Error(message = response.errorBody().toString())
        }
    }
}