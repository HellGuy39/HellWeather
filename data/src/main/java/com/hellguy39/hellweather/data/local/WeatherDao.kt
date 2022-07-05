package com.hellguy39.hellweather.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface WeatherDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWeather(
        weatherEntity: WeatherForecastEntity
    )

    @Query("SELECT * FROM WeatherForecastEntity")
    suspend fun getWeatherList() : List<WeatherForecastEntity>

    @Query("DELETE FROM WeatherForecastEntity")
    suspend fun clearWeather()

}