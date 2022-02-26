package com.hellguy39.hellweather.domain.repository

import com.hellguy39.hellweather.domain.models.param.UserLocationParam

interface LocationRepository {

    suspend fun insertLocation(userLocationParam: UserLocationParam)

    suspend fun deleteLocation(userLocationParam: UserLocationParam)

    suspend fun getLocationById(id: Int) : UserLocationParam?

    suspend fun getLocations(): List<UserLocationParam>

}