package com.hellguy39.hellweather.domain.usecase.local

import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.repository.LocationRepository
import com.hellguy39.hellweather.domain.resource.Resource

class GetUserLocationListUseCase(private val locationRepository: LocationRepository) {

    suspend operator fun invoke(): Resource<List<UserLocationParam>> {
        val list = locationRepository.getLocations()

        return if (!list.isNullOrEmpty())
            Resource.Success(list)
        else
            Resource.Error("Empty list")

    }
}