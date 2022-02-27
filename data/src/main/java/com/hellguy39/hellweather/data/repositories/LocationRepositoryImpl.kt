package com.hellguy39.hellweather.data.repositories

import com.hellguy39.hellweather.data.db.LocationDao
import com.hellguy39.hellweather.data.utils.ModelConverter
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.repository.LocationRepository
import kotlinx.coroutines.flow.first

class LocationRepositoryImpl(
    private val dao: LocationDao
): LocationRepository {

    override suspend fun insertLocation(userLocationParam: UserLocationParam) {
        dao.insertLocation(ModelConverter.toEntity(userLocationParam))
    }

    override suspend fun deleteLocation(userLocationParam: UserLocationParam) {
        dao.deleteLocation(ModelConverter.toEntity(userLocationParam))
    }

    override suspend fun getLocationById(id: Int): UserLocationParam {
        val userLocation = dao.getLocationById(id)
        if (userLocation != null)
            return ModelConverter.toParam(userLocation)
        else
            return UserLocationParam()
    }

    override suspend fun getLocations(): List<UserLocationParam> {

        val list = dao.getLocations().first()
        val listParam = mutableListOf<UserLocationParam>()

        for (n in list.indices) {
            listParam.add(ModelConverter.toParam(list[n]))
        }

        return listParam
    }

}