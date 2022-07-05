package com.hellguy39.hellweather.data.json

import com.hellguy39.hellweather.data.remote.dto.OneCallWeatherDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import okhttp3.ResponseBody

class OneCallWeatherParser(
    moshi: Moshi
) {
    private val jsonAdapter: JsonAdapter<OneCallWeatherDto> =
        moshi.adapter(OneCallWeatherDto::class.java)

    fun parse(responseBody: ResponseBody): OneCallWeatherDto? {
        return jsonAdapter.fromJson(responseBody.string())
    }

    fun parse(response: String?): OneCallWeatherDto? {
        return response?.let { jsonAdapter.fromJson(it) }
    }
}