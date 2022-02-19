package com.hellguy39.hellweather.presentation.fragments.search

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val mService: ApiService,
    private val defSharedPrefs: SharedPreferences
): ViewModel() {

    private val lang = Locale.getDefault().country
    val currentWeatherLive = MutableLiveData<CurrentWeather>()
    val statusLive = MutableLiveData<String>()

    fun getUnits(): String = defSharedPrefs.getString(PREFS_UNITS, METRIC).toString()

    fun getCurrentWeather(cityName: String) {
        /*viewModelScope.launch(Dispatchers.IO) {
            statusLive.value = IN_PROGRESS
            val request = sendRequest(cityName)
            val converter = Converter()

            if (converter.checkRequest(request) == FAILURE)
            {
                statusLive.value = FAILURE
                return@launch
            }
            else if (converter.checkRequest(request) == INCORRECT_OBJ)
            {
                statusLive.value = ERROR
                return@launch
            }

            val currentWeather = converter.toCurrentWeather(request)
            currentWeatherLive.value = currentWeather
            statusLive.value = SUCCESSFUL
        }*/
    }

    /*private suspend fun sendRequest(cityName: String): JsonObject {
        val units: String = defSharedPrefs.getString(PREFS_UNITS, METRIC).toString()
        return suspendCoroutine { continuation ->
            mService.getCurrentWeather(
                cityName,
                units,
                lang,
                OPEN_WEATHER_API_KEY
            ).enqueue(object : Callback<JsonObject> {
                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {
                    if (response.isSuccessful)
                    {
                        Log.d("DEBUG", call.request().url.toString())
                        if (response.body() != null) {
                            continuation.resume(response.body() as JsonObject)
                        } else {
                            continuation.resume(JsonObject().apply { addProperty("request", INCORRECT_OBJ) })
                        }
                    }
                    else
                    {
                        continuation.resume(JsonObject().apply { addProperty("request", INCORRECT_OBJ) })
                    }
                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    continuation.resume(JsonObject().apply { addProperty("request", FAILURE) })
                }

            })
        }
    }*/

}