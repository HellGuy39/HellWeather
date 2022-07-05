package com.hellguy39.hellweather.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [WeatherForecastEntity::class],
    version = 1
)
abstract class WeatherDatabase: RoomDatabase() {
    abstract val dao: WeatherDao
}