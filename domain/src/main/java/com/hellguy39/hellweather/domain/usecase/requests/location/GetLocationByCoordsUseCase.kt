package com.hellguy39.hellweather.domain.usecase.requests.location

import com.google.gson.JsonObject
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.repository.ApiRepository
import com.hellguy39.hellweather.domain.resource.Resource
import com.hellguy39.hellweather.domain.utils.converter
import com.hellguy39.hellweather.domain.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.domain.utils.Unit
import retrofit2.Response
import java.lang.Exception
import java.util.*

class GetLocationByCoordsUseCase(private val apiRepositoryImpl: ApiRepository) {
    suspend operator fun invoke(lat: Double, lon: Double): Resource<UserLocationParam> {

        val response: Response<JsonObject>

        try {
            response = apiRepositoryImpl.getCurrentWeatherWithLatLon(
                lat = lat,
                lon = lon,
                units = Unit.Metric.name,
                lang = Locale.getDefault().country,
                appId = OPEN_WEATHER_API_KEY
            )
        } catch (e: Exception) {
            return Resource.Error(message = "No internet connection")
        }

        return if (response.isSuccessful) {
            val jsonObject = response.body()

            if (jsonObject != null) {
                Resource.Success(data = converter().toUserLocationParam(jsonObject))
            } else {
                Resource.Error(message = "Response body is null")
            }
        } else {
            Resource.Error(message = response.errorBody().toString())
        }
    }
}