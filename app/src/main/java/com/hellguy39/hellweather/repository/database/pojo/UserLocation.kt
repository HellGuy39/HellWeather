package com.hellguy39.hellweather.repository.database.pojo

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserLocation(
    var lat: String = "N/A",
    var lon: String = "N/A",
    var requestName: String = "N/A",
    var locationName: String = "N/A",
    var country: String = "N/A",
    var cod: String = "N/A",
    var id: Int = 0,
    var timezone: Int = 0
) : Parcelable
