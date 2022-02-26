package com.hellguy39.hellweather.domain.usecase.prefs.service

data class ServiceUseCases(
    val saveServiceLocationUseCase: SaveServiceLocationUseCase,
    val saveServiceModeUseCase: SaveServiceModeUseCase,
    val saveServiceUpdateTimeUseCase: SaveServiceUpdateTimeUseCase,
    val getServiceLocationUseCase: GetServiceLocationUseCase,
    val getServiceModeUseCase: GetServiceModeUseCase,
    val getServiceUpdateTimeUseCase: GetServiceUpdateTimeUseCase
)
