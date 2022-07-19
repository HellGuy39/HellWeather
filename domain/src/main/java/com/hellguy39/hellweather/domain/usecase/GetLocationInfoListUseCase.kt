package com.hellguy39.hellweather.domain.usecase

import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class GetLocationInfoListUseCase(
    private val remoteRepo: RemoteRepository
) {
    suspend operator fun invoke(cityName: String): Flow<Resource<List<LocationInfo>>> {
        return flow {
            emit(Resource.Loading(true))

            val data = remoteRepo.getLocationsInfoByCityName(cityName, LIMIT)
            emit(data)

            emit(Resource.Loading(false))
            return@flow
        }
    }

    companion object {
        private const val LIMIT = 5
    }
}