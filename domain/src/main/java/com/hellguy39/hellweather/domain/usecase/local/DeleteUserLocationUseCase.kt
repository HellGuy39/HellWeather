package com.hellguy39.hellweather.domain.usecase.local

import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.repository.LocationRepository

class DeleteUserLocationUseCase(private val locationRepository: LocationRepository) {
    suspend operator fun invoke(model: UserLocationParam) {
        locationRepository.deleteLocation(userLocationParam = model)
    }
}