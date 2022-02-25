package com.hellguy39.hellweather.data.utils

import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.models.UserLocationParam

object ModelConverter {
    fun toEntity(param: UserLocationParam): UserLocation {
        return UserLocation(
            lat = param.lat,
            lon = param.lon,
            locationName = param.locationName,
            country = param.country,
            cod = param.cod,
            timezone = param.timezone
        )
    }
    fun toParam(ul: UserLocation): UserLocationParam {
        return UserLocationParam(
            lat = ul.lat,
            lon = ul.lon,
            locationName = ul.locationName,
            country = ul.country,
            cod = ul.cod,
            timezone = ul.timezone,
            id = ul.id ?: 0
        )
    }
}