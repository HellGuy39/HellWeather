package com.hellguy39.hellweather.domain.repository

import com.google.gson.JsonObject
import retrofit2.Response

interface ApiRepository {

    suspend fun getCurrentWeather(
        cityName: String, units: String, lang: String, appId: String
    ): Response<JsonObject>

    suspend fun getCurrentWeatherWithLatLon(
        lat: Double, lon: Double, units: String, lang: String, appId: String
    ): Response<JsonObject>

    suspend fun getWeatherOneCall(
        lat: Double, lon: Double, exclude: String, units: String, lang: String, appId:String
    ): Response<JsonObject>

}