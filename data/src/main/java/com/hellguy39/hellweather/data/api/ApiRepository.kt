package com.hellguy39.hellweather.data.api

import com.google.gson.JsonObject
import retrofit2.Response
import javax.inject.Inject

class ApiRepository @Inject constructor(private val apiService: ApiService) {

    suspend fun getCurrentWeather(
        cityName: String, units: String, lang: String, appId: String
    ): Response<JsonObject> = apiService.getCurrentWeather(cityName, units, lang, appId)

    suspend fun getCurrentWeatherWithLatLon(
        lat: Double, lon: Double, units: String, lang: String, appId: String
    ): Response<JsonObject> = apiService.getCurrentWeatherWithLatLon(lat, lon, units, lang, appId)

    suspend fun getWeatherOneCall(
        lat: Double, lon: Double, exclude: String, units: String, lang: String, appId:String
    ): Response<JsonObject> = apiService.getWeatherOneCall(lat,lon,exclude,units,lang,appId)

}