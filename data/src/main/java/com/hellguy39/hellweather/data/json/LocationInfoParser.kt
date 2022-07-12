package com.hellguy39.hellweather.data.json

import com.hellguy39.hellweather.data.remote.dto.LocationInfoDto
import com.hellguy39.hellweather.domain.model.LocationInfo
import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.Types
import okhttp3.ResponseBody
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationInfoParser @Inject constructor(
    moshi: Moshi
) {

    private val jsonAdapter: JsonAdapter<List<LocationInfoDto>> = moshi.adapter(
        Types.newParameterizedType(
            List::class.java,
            LocationInfoDto::class.java
        )
    )

    fun parseFromJson(responseBody: ResponseBody): List<LocationInfoDto>? {
        return jsonAdapter.fromJson(responseBody.string())
    }

    fun parseFromJson(response: String?): List<LocationInfoDto>? {
        return response?.let { jsonAdapter.fromJson(it) }
    }

    fun parseToJson(locationInfoDto: List<LocationInfoDto>?): String? {
        return locationInfoDto?.let { jsonAdapter.toJson(locationInfoDto) }
    }
}