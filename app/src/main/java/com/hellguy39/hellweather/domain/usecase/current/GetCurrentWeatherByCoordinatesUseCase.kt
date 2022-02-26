package com.hellguy39.hellweather.domain.usecase.current

/*
class GetCurrentWeatherByCoordinatesUseCase @Inject constructor(private val apiRepositoryImpl: ApiRepositoryImpl) {
    suspend fun execute(model: CurrentByCoordinatesRequest): CurrentWeather {
        var currentWeather = CurrentWeather()
        val converter = Converter()

        val response = apiRepositoryImpl.getCurrentWeatherWithLatLon(
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
