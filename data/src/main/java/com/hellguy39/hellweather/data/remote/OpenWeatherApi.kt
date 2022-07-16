package com.hellguy39.hellweather.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Query

interface OpenWeatherApi {

    @GET("data/2.5/onecall?")
    suspend fun getOneCall(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("appid") apiKey: String,
        @Query("exclude") exclude: String,
        @Query("units") units: String
        //@Query("lang") lang: String
    ): ResponseBody

    @GET("geo/1.0/reverse?")
    suspend fun getLocationsInfoByCoordinates(
        @Query("lat") lat: Double,
        @Query("lon") lon: Double,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String,
    ): ResponseBody

    @GET("geo/1.0/direct?")
    suspend fun getLocationsInfoByCityName(
        @Query("q") cityName: String,
        @Query("limit") limit: Int,
        @Query("appid") apiKey: String,
    ): ResponseBody

    companion object {
        const val API_KEY = "110d89c61036b9213b828f8fbd49d1cc"
        const val BASE_URL = "https://api.openweathermap.org/"
    }

}