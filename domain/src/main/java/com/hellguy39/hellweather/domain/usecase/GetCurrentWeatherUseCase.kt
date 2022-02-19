package com.hellguy39.hellweather.domain.usecase

import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class GetCurrentWeatherUseCase() {


    fun execute() {

    }

   /* private suspend fun sendRequestWithLatLon(lat: Double, lon: Double) : JsonObject {
        return suspendCoroutine { continuation ->
            mService.getCurrentWeatherWithLatLon(
                lat,
                lon,
                METRIC,
                lang,
                OPEN_WEATHER_API_KEY
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(
                    call: Call<JsonObject>,
                    response: Response<JsonObject>
                ) {

                    Log.d("DEBUG", call.request().url.toString())

                    if (response.body() != null) {
                        continuation.resume(response.body() as JsonObject)
                    } else {
                        continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                }
            })
        }
    }

    private suspend fun sendRequestWithCity(input: String) : JsonObject {
        return suspendCoroutine { continuation ->
            mService.getCurrentWeather(
                input,
                METRIC,
                lang,
                OPEN_WEATHER_API_KEY)
                .enqueue(object : Callback<JsonObject> {

                    override fun onResponse(
                        call: Call<JsonObject>,
                        response: Response<JsonObject>
                    ) {

                        Log.d("DEBUG", call.request().url.toString())

                        if (response.isSuccessful) {
                            if (response.body() != null) {
                                continuation.resume(response.body() as JsonObject)
                            } else {
                                continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                            }
                        } else {
                            continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                        }

                    }

                    override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                        continuation.resume(JsonObject().apply { addProperty("request", "failed") })
                    }
                })
        }
    }*/
}