package com.hellguy39.hellweather.repository.database

import com.hellguy39.hellweather.repository.database.dao.LocationDao
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import kotlinx.coroutines.flow.Flow

class LocationRepositoryImpl(
    private val dao: LocationDao
): LocationRepository {

    override suspend fun insertLocation(userLocation: UserLocation) {
        dao.insertLocation(userLocation)
    }

    override suspend fun deleteLocation(userLocation: UserLocation) {
        dao.deleteLocation(userLocation)
    }

    override suspend fun getLocationById(id: Int): UserLocation? {
        return dao.getLocationById(id)
    }

    override fun getLocations(): Flow<List<UserLocation>> {
        return dao.getLocations()
    }

}