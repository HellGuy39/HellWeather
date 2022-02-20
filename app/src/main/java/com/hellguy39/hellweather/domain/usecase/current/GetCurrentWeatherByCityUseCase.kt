package com.hellguy39.hellweather.domain.usecase.current

import com.hellguy39.hellweather.data.repositories.ApiRepository
import com.hellguy39.hellweather.domain.models.CurrentByCityRequest
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.utils.Converter
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.SUCCESSFUL
import javax.inject.Inject

class GetCurrentWeatherByCityUseCase @Inject constructor(private val apiRepository: ApiRepository) {
    suspend fun execute(model: CurrentByCityRequest): CurrentWeather {
        var currentWeather = CurrentWeather()
        val converter = Converter()

        val response = apiRepository.getCurrentWeather(
            model.cityName,
            model.units,
            model.lang,
            model.appId
        )
        if (response.isSuccessful) {
            val jsonObject = response.body()
            if (jsonObject != null)
            {
                currentWeather = converter.toCurrentWeather(jsonObject)
                currentWeather.requestResult = SUCCESSFUL
            }
            else
            {
                currentWeather.requestResult = ERROR
                currentWeather.errorBody = "Response body is null"
            }
        }
        else
        {
            currentWeather.requestResult = ERROR
            currentWeather.errorBody = response.errorBody().toString()
        }

        return currentWeather
    }
}