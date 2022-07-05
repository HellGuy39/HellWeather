package com.hellguy39.hellweather.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class WeatherForecastEntity(
    @PrimaryKey val id: Int? = null,
    val weatherJson: String?,
    val locationJson: String
)
