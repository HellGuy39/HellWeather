package com.hellguy39.hellweather.data.api

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall?")
    suspend fun getWeatherOneCall(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("exclude")exclude: String,
        @Query("units")units: String,
        @Query("lang")lang: String,
        @Query("appid")appId: String
    ): Response<JsonObject>

    @GET("weather?")
    suspend fun getCurrentWeather(
        @Query("q")city: String,
        @Query("units")units: String,
        @Query("lang")lang: String,
        @Query("appid")appId: String
    ): Response<JsonObject>

    @GET("weather?")
    suspend fun getCurrentWeatherWithLatLon(
        @Query("lat")lan: Double,
        @Query("lon")lon: Double,
        @Query("units")units: String,
        @Query("lang")lang: String,
        @Query("appid")appId: String
    ): Response<JsonObject>
}