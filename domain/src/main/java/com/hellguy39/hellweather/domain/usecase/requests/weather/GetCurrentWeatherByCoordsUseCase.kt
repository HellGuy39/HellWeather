package com.hellguy39.hellweather.domain.usecase.requests.weather

import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.models.request.CurrentByCoordinatesRequest
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter

class GetCurrentWeatherByCoordsUseCase(private val apiRepositoryImpl: ApiRepository) {
    suspend operator fun invoke(model: CurrentByCoordinatesRequest): Resource<CurrentWeather> {

        val response = apiRepositoryImpl.getCurrentWeatherWithLatLon(
            lat = model.lat,
            lon = model.lon,
            units = model.units,
            lang = model.lang,
            appId = model.appId
        )

        if (response.isSuccessful) {
            val jsonObject = response.body()

            if (jsonObject != null) {
                return Resource.Success(converter().toCurrentWeather(jsonObject))
            } else {
                return Resource.Error("Response body is null")
            }
        } else {
            return Resource.Error(response.errorBody().toString())
        }
    }
}