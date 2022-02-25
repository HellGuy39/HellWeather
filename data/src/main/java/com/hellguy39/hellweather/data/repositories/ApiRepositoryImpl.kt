package com.hellguy39.hellweather.data.repositories

import com.google.gson.JsonObject
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.domain.repository.ApiRepository
import retrofit2.Response
import javax.inject.Inject

class ApiRepositoryImpl @Inject constructor(private val apiService: ApiService): ApiRepository {

    override suspend fun getCurrentWeather(
        cityName: String,
        units: String,
        lang: String,
        appId: String
    ): Response<JsonObject> = apiService.getCurrentWeather(cityName, units, lang, appId)

    override suspend fun getCurrentWeatherWithLatLon(
        lat: Double,
        lon: Double,
        units: String,
        lang: String,
        appId: String
    ): Response<JsonObject> = apiService.getCurrentWeatherWithLatLon(lat, lon, units, lang, appId)

    override suspend fun getWeatherOneCall(
        lat: Double,
        lon: Double,
        exclude: String,
        units: String,
        lang: String,
        appId:String
    ): Response<JsonObject> = apiService.getWeatherOneCall(lat,lon,exclude,units,lang,appId)

}