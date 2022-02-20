package com.hellguy39.hellweather.domain.usecase.locations

import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.data.repositories.LocationRepository
import kotlinx.coroutines.flow.first
import javax.inject.Inject

class GetUserLocationsUseCase @Inject constructor(private val repository: LocationRepository){
    suspend fun execute(): List<UserLocation> {
        return repository.getLocations().first()
    }
}