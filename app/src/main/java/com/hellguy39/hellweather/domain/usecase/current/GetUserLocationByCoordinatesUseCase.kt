package com.hellguy39.hellweather.domain.usecase.current

/*
class GetUserLocationByCoordinatesUseCase @Inject constructor(private val apiRepositoryImpl: ApiRepositoryImpl) {
    suspend fun execute(model: CurrentByCoordinatesRequest): UserLocation {
        var userLocation = UserLocation()
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
