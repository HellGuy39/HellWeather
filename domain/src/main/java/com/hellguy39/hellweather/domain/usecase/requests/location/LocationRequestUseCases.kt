package com.hellguy39.hellweather.domain.usecase.requests.location

data class LocationRequestUseCases(
    val getLocationByCityNameUseCase: GetLocationByCityNameUseCase,
    val getLocationByCoordsUseCase: GetLocationByCoordsUseCase
)
