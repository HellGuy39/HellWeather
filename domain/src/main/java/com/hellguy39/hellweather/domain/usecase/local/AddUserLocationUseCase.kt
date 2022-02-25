package com.hellguy39.hellweather.domain.usecase.local

import com.hellguy39.hellweather.domain.models.UserLocationParam
import com.hellguy39.hellweather.domain.repository.LocationRepository

class AddUserLocationUseCase (private val locationRepository: LocationRepository) {
    suspend operator fun invoke(userLocationParam: UserLocationParam) {
        locationRepository.insertLocation(userLocationParam)
    }
}