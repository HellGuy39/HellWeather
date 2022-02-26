package com.hellguy39.hellweather.domain.usecase.current

/*
import com.hellguy39.hellweather.data.repositories.ApiRepositoryImpl
import com.hellguy39.hellweather.domain.models.request.CurrentByCityRequest
import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.utils.Converter
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.SUCCESSFUL
import javax.inject.Inject

class GetCurrentWeatherByCityUseCase @Inject constructor(private val apiRepositoryImpl: ApiRepositoryImpl) {
    suspend fun execute(model: CurrentByCityRequest): CurrentWeather {
        var currentWeather = CurrentWeather()
        val converter = Converter

        val response = apiRepositoryImpl.getCurrentWeather(
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
}*/
