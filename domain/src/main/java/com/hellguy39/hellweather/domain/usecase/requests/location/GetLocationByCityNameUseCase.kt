package com.hellguy39.hellweather.domain.usecase.requests.location

import com.hellguy39.hellweather.domain.models.UserLocationParam
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import java.util.*

class GetLocationByCityNameUseCase(private val apiRepositoryImpl: ApiRepository) {
    suspend operator fun invoke(cityName: String): Resource<UserLocationParam> {

        val response = apiRepositoryImpl.getCurrentWeather(
            cityName = cityName,
            units = METRIC,
            lang = Locale.getDefault().country,
            appId = OPEN_WEATHER_API_KEY
        )

        if (response.isSuccessful) {
            val jsonObject = response.body()

            if (jsonObject != null) {
                return Resource.Success(converter().toUserLocationParam(jsonObject))
            } else {
                return Resource.Error("Response body is null")
            }
        } else {
            return Resource.Error(response.errorBody().toString())
        }

    }
}