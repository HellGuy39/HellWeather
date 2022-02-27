package com.hellguy39.hellweather.data.enteties

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserLocation(
    var lat: Double = 0.0,
    var lon: Double = 0.0,
    var locationName: String = "N/A",
    var country: String = "N/A",
    var cod: String = "N/A",
    var timezone: Int = 0,
    @PrimaryKey val id: Int? = null
) : Parcelable
