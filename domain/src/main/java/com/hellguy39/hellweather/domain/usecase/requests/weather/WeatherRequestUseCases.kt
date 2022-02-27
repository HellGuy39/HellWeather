package com.hellguy39.hellweather.domain.usecase.requests.weather

data class WeatherRequestUseCases(
    val getOneCallWeatherUseCase: GetOneCallWeatherUseCase,
    val getCurrentWeatherByCityNameUseCase: GetCurrentWeatherByCityNameUseCase,
    val getCurrentWeatherByCoordsUseCase: GetCurrentWeatherByCoordsUseCase,
)
