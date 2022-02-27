package com.hellguy39.hellweather.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.hellguy39.hellweather.data.enteties.UserLocation

@Database(
    entities = [UserLocation::class],
    exportSchema = false,
    version = 1
)

abstract class LocationDatabase : RoomDatabase() {

    abstract val dao: LocationDao

}