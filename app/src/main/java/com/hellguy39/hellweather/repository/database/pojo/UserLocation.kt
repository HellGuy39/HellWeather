package com.hellguy39.hellweather.repository.database.pojo

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity
@Parcelize
data class UserLocation(
    var lat: String = "N/A",
    var lon: String = "N/A",
    var requestName: String = "N/A",
    var locationName: String = "N/A",
    var country: String = "N/A",
    var cod: String = "N/A",
    var timezone: Int = 0,
    @PrimaryKey val id: Int? = null
) : Parcelable
