package com.hellguy39.hellweather.repository.server

import com.google.gson.JsonObject
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {

    @GET("onecall?")
    fun getWeatherOneCall(
        @Query("lat")lat: Double,
        @Query("lon")lon: Double,
        @Query("exclude")exclude: String,
        @Query("units")units: String,
        @Query("lang")lang: String,
        @Query("appid")appId: String
    ): Call<JsonObject>

    @GET("weather?")
    fun getCurrentWeather(
        @Query("q")city: String,
        @Query("units")units: String,
        @Query("appid")appId: String
    ): Call<JsonObject>
}