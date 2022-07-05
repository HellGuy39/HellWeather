package com.hellguy39.hellweather.data.mapper

import com.hellguy39.hellweather.data.remote.dto.LocationInfoDto
import com.hellguy39.hellweather.domain.model.LocationInfo

fun LocationInfoDto.toLocationInfo() : LocationInfo{
    return LocationInfo(
        lat = lat,
        lon = lon,
        name = name,
        localNames = localNames,
        country = country,
        state = state
    )
}