package com.hellguy39.hellweather.data.json

import com.hellguy39.hellweather.data.remote.dto.OneCallWeatherDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class OneCallWeatherParser @Inject constructor (
    moshi: Moshi
) {
    private val jsonAdapter: JsonAdapter<OneCallWeatherDto> =
        moshi.adapter(OneCallWeatherDto::class.java)

    fun parseFromJson(responseBody: ResponseBody): OneCallWeatherDto? {
        return jsonAdapter.fromJson(responseBody.string())
    }

    fun parseFromJson(response: String?): OneCallWeatherDto? {
        return response?.let { jsonAdapter.fromJson(it) }
    }

    fun parseToJson(oneCallWeatherDto: OneCallWeatherDto?): String? {
        return jsonAdapter.toJson(oneCallWeatherDto)
    }

}