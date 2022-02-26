package com.hellguy39.hellweather.domain.usecase.requests.weather

import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.models.request.CurrentByCityRequest
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter

class GetCurrentWeatherByCityNameUseCase(private val apiRepositoryImpl: ApiRepository) {
    suspend operator fun invoke(model: CurrentByCityRequest) : Resource<CurrentWeather> {

        val response = apiRepositoryImpl.getCurrentWeather(
            cityName = model.cityName,
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