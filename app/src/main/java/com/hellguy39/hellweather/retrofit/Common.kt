package com.hellguy39.hellweather.retrofit

import com.hellguy39.hellweather.utils.BASE_URL

object Common {
    val retrofitServices: RetrofitServices
        get() = RetrofitClient.getClient(BASE_URL).create(RetrofitServices::class.java)
}