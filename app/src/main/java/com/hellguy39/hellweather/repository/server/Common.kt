package com.hellguy39.hellweather.repository.server

import com.hellguy39.hellweather.utils.BASE_URL

object Common {
    val retrofitServices: ApiService
        get() = RetrofitClient.getClient(BASE_URL).create(ApiService::class.java)
}