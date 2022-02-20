package com.hellguy39.hellweather.domain.usecase.current

import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.data.repositories.ApiRepository
import com.hellguy39.hellweather.domain.models.CurrentByCoordinatesRequest
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.utils.Converter
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.SUCCESSFUL
import javax.inject.Inject

class GetUserLocationByCoordinatesUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend fun execute(model: CurrentByCoordinatesRequest): UserLocation {
        var userLocation = UserLocation()
        val converter = Converter()

        val response = apiRepository.getCurrentWeatherWithLatLon(
            model.lat,
            model.lon,
            model.units,
            model.lang,
            model.appId
        )

        if (response.isSuccessful) {
            val jsonObject = response.body()
            if (jsonObject != null)
            {
                userLocation = converter.toUserLocation(jsonObject)
                userLocation.requestResult = SUCCESSFUL
            }
            else
            {
                userLocation.requestResult = ERROR
                userLocation.errorBody = "Response body is null"
            }
        }
        else
        {
            userLocation.requestResult = ERROR
            userLocation.errorBody = response.errorBody().toString()
        }

        return userLocation
    }
}