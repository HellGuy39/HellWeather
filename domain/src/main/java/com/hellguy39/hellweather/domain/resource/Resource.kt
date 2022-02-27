package com.hellguy39.hellweather.domain.resource

typealias SimpleResource = Resource<Unit>

sealed class Resource<T>(
    val data: T? = null,
    val message: String? = null
) {
    class Success<T>(
        data: T?
    ): Resource<T>(data)

    class Error<T>(
        message: String,
        data: T? = null
    ): Resource<T>(data, message)
}
