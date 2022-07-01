package com.hellguy39.hellweather.data.mapper

import com.hellguy39.hellweather.data.remote.dto.LocationNameDto
import com.hellguy39.hellweather.domain.model.LocationName

fun LocationNameDto.toLocationName() : LocationName{
    return LocationName(
        lat = lat,
        lon = lon,
        name = name,
        localNames = localNames,
        country = country
    )
}