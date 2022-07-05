package com.hellguy39.hellweather.data.json

import com.hellguy39.hellweather.data.remote.dto.LocationInfoDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.ResponseBody

class LocationInfoParser(
    private val moshi: Moshi
) {

    private val jsonAdapter: JsonAdapter<List<LocationInfoDto>> = moshi.adapter(
        Types.newParameterizedType(
            List::class.java,
            LocationInfoDto::class.java
        )
    )

    fun parse(responseBody: ResponseBody): List<LocationInfoDto>? {
        return jsonAdapter.fromJson(responseBody.string())
    }

    fun parse(response: String?): List<LocationInfoDto>? {
        return response?.let { jsonAdapter.fromJson(it) }
    }
}