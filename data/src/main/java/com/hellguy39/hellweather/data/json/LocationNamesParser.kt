package com.hellguy39.hellweather.data.json

import com.hellguy39.hellweather.data.remote.dto.LocationNameDto
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.ResponseBody

class LocationNameParser(
    private val moshi: Moshi
) {

    private val jsonAdapter: JsonAdapter<List<LocationNameDto>> = moshi.adapter(
        Types.newParameterizedType(
            List::class.java,
            LocationNameDto::class.java
        )
    )

    fun parse(responseBody: ResponseBody): List<LocationNameDto>? {
        return jsonAdapter.fromJson(responseBody.string())
    }
}