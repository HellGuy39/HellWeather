package com.hellguy39.hellweather.domain.usecase.requests.weather

import com.google.gson.JsonObject
import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.models.request.CurrentByCoordinatesRequest
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter
import retrofit2.Response
import java.lang.Exception

class GetCurrentWeatherByCoordsUseCase(private val apiRepositoryImpl: ApiRepository) {
    suspend operator fun invoke(model: CurrentByCoordinatesRequest): Resource<CurrentWeather> {

        val response: Response<JsonObject>

        try {
            response = apiRepositoryImpl.getCurrentWeatherWithLatLon(
                lat = model.lat,
                lon = model.lon,
                units = model.units,
                lang = model.lang,
                appId = model.appId
            )
        } catch (e: Exception) {
            return Resource.Error(message = "No internet connection")
        }

        return if (response.isSuccessful) {
            val jsonObject = response.body()

            if (jsonObject != null) {
                Resource.Success(data = converter().toCurrentWeather(jsonObject))
            } else {
                Resource.Error(message = "Response body is null")
            }
        } else {
            Resource.Error(message = response.errorBody().toString())
        }
    }
}