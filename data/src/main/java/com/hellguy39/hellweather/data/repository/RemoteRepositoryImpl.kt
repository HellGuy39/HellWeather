package com.hellguy39.hellweather.data.repository

import com.hellguy39.hellweather.data.json.LocationInfoParser
import com.hellguy39.hellweather.data.json.OneCallWeatherParser
import com.hellguy39.hellweather.data.mapper.toLocationInfo
import com.hellguy39.hellweather.data.mapper.toOneCallWeather
import com.hellguy39.hellweather.data.remote.OpenWeatherApi
import com.hellguy39.hellweather.domain.model.LocationInfo
import com.hellguy39.hellweather.domain.model.OneCallWeather
import com.hellguy39.hellweather.domain.repository.RemoteRepository
import com.hellguy39.hellweather.domain.util.Resource
import retrofit2.HttpException
import java.io.IOException

class RemoteRepositoryImpl(
    private val api: OpenWeatherApi,
    private val oneCallWeatherParser: OneCallWeatherParser,
    private val locationInfoParser: LocationInfoParser
): RemoteRepository {

    override suspend fun getOneCallWeather(
        lat: Double,
        lon: Double
    ): Resource<OneCallWeather> {
        try {
            val response = api.getOneCall(
                lat = lat,
                lon = lon,
                apiKey = OpenWeatherApi.API_KEY,
                exclude = "minutely",
                units = "metric"
            )

            val data = oneCallWeatherParser.parseFromJson(response)

            return if (data != null)
                Resource.Success(data.toOneCallWeather())
            else
                Resource.Error(message = "Couldn't load data")

        } catch (e: IOException) {
            e.printStackTrace()
            return Resource.Error(message = "Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            return Resource.Error(message = "Couldn't load data")
        }
    }

    override suspend fun getLocationInfo(
        lat: Double,
        lon: Double,
        limit: Int
    ): Resource<List<LocationInfo>> {
        try {
            val response = api.getLocationInfo(
                lat = lat,
                lon = lon,
                limit = limit,
                apiKey = OpenWeatherApi.API_KEY
            )

            val data = locationInfoParser.parseFromJson(response)

            return if (data != null)
                Resource.Success(data.map { it.toLocationInfo() })
            else
                Resource.Error(message = "Couldn't load data")
        } catch (e: IOException) {
            e.printStackTrace()
            return Resource.Error(message = "Couldn't load data")
        } catch (e: HttpException) {
            e.printStackTrace()
            return Resource.Error(message = "Couldn't load data")
        }
    }
}