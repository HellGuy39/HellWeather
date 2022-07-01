package com.hellguy39.hellweather.data.repository

import android.util.Log
import com.hellguy39.hellweather.data.mapper.toLocationName
import com.hellguy39.hellweather.data.mapper.toOneCallWeather
import com.hellguy39.hellweather.data.remote.OpenWeatherApi
import com.hellguy39.hellweather.data.remote.dto.LocationNameDto
import com.hellguy39.hellweather.data.remote.dto.OneCallWeatherDto
import com.hellguy39.hellweather.domain.model.LocationName
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.repository.OpenWeatherRepository
import com.hellguy39.hellweather.domain.util.Resource
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import javax.inject.Inject

class OpenWeatherRepositoryImpl(
    private val api: OpenWeatherApi
): OpenWeatherRepository {

    override suspend fun getOneCallWeather(
        fetchFromRemote: Boolean,
        lat: Double,
        lon: Double
    ): Flow<Resource<OneCallWeather>> {
        return flow {
            emit(Resource.Loading(true))
            try {
                val response = api.getOneCall(lat = lat, lon = lon, apiKey = OpenWeatherApi.API_KEY, exclude = "minutely", units = "metric")

                val moshi: Moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<OneCallWeatherDto> = moshi.adapter(OneCallWeatherDto::class.java)
                val data = jsonAdapter.fromJson(response.string())

                if (data != null)
                    emit(Resource.Success(data.toOneCallWeather()))
                else
                    emit(Resource.Error("Couldn't load data"))


            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Couldn't load data"))
            }

            //emit(Resource.Loading(false))

            return@flow
        }
    }

    override suspend fun getLocationName(
        lat: Double,
        lon: Double,
        limit: Int
    ): Flow<Resource<List<LocationName>>> {
        return flow {
            try {
                val response = api.getLocationName(lat = lat, lon = lon, limit = limit, apiKey = OpenWeatherApi.API_KEY)

                val moshi: Moshi = Moshi.Builder().build()
                val jsonAdapter: JsonAdapter<List<LocationNameDto>> = moshi.adapter(Types.newParameterizedType(List::class.java, LocationNameDto::class.java))
                val data = jsonAdapter.fromJson(response.string())

                if (data != null)
                    emit(Resource.Success(data.map { it.toLocationName() }))
                else
                    emit(Resource.Error("Unknown"))


            } catch (e: IOException) {
                e.printStackTrace()
                emit(Resource.Error("Unknown"))
            } catch (e: HttpException) {
                e.printStackTrace()
                emit(Resource.Error("Unknown"))
            }
        }
    }
}