package com.hellguy39.hellweather.core.network.util

sealed class RequestResult<T>{

    data class Success<T>(val data: T): RequestResult<T>()

    data class Error<T>(val errorMessage: String): RequestResult<T>()

}