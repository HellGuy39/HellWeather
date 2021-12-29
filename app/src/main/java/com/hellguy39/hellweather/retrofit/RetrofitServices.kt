package com.hellguy39.hellweather.retrofit

import com.google.gson.JsonObject
import com.hellguy39.hellweather.utils.WeatherModel
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface RetrofitServices {
    @GET("onecall?")
    fun getWeather(
        @Query("lat")lat: Int,
        @Query("lon")lon: Int,
        @Query("exclude")exclude: String,
        @Query("units")units: String,
        @Query("appid")appId: String
    ): Call<JsonObject>
}