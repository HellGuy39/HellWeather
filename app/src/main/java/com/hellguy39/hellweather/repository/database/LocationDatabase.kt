package com.hellguy39.hellweather.repository.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.hellweather.repository.database.dao.LocationDao
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

@Database(
    entities = [UserLocation::class],
    version = 1
)

abstract class LocationDatabase : RoomDatabase() {

    abstract val dao: LocationDao

}