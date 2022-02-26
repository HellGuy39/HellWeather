package com.hellguy39.hellweather.domain.usecase.current

/*
class GetUserLocationByCityUseCase @Inject constructor(private val apiRepositoryImpl: ApiRepositoryImpl) {
    suspend fun execute(model: CurrentByCityRequest): UserLocation {
        var userLocation = UserLocation()
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
}*/
