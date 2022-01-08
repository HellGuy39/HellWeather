package com.hellguy39.hellweather.repository.database

import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import kotlinx.coroutines.flow.Flow

interface LocationRepository {

    suspend fun insertLocation(userLocation: UserLocation)

    suspend fun deleteLocation(userLocation: UserLocation)

    suspend fun getLocationById(id: Int) : UserLocation?

    fun getLocations(): Flow<List<UserLocation>>

}