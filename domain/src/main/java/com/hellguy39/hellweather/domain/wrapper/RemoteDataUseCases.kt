package com.hellguy39.hellweather.domain.wrapper

import com.hellguy39.hellweather.domain.usecase.GetLocationNameUseCase
import com.hellguy39.hellweather.domain.usecase.GetOneCallWeatherUseCase

data class RemoteDataUseCases(
    val getLocationName: GetLocationNameUseCase,
    val getOneCallWeather: GetOneCallWeatherUseCase
)
