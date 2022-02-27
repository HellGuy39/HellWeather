package com.hellguy39.hellweather.data.db

import androidx.room.*
import com.hellguy39.hellweather.data.enteties.UserLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface LocationDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertLocation(userLocation: UserLocation)

    @Delete
    suspend fun deleteLocation(userLocation: UserLocation)

    @Query("SELECT * FROM UserLocation WHERE id = :id")
    suspend fun getLocationById(id: Int) : UserLocation?

    @Query("SELECT * FROM UserLocation")
    fun getLocations(): Flow<List<UserLocation>>

}