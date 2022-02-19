package com.hellguy39.hellweather.data.repositories

import com.hellguy39.hellweather.data.enteties.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun insertLocation(userLocation: UserLocation)

    suspend fun deleteLocation(userLocation: UserLocation)

    suspend fun getLocationById(id: Int) : UserLocation?

    fun getLocations(): Flow<List<UserLocation>>

}