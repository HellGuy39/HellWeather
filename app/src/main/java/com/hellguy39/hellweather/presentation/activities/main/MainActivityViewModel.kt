package com.hellguy39.hellweather.presentation.activities.main

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: LocationRepository,
    private val mService: ApiService
): ViewModel() {

    val userLocationsLive = MutableLiveData<List<UserLocation>>()
    val weatherJsonListLive = MutableLiveData<List<JsonObject>>()
    val statusLive = MutableLiveData<String>()
    private val jsonList: MutableList<JsonObject> = mutableListOf()

    init {
        //weatherJsonListLive.value?.add(JsonObject()) // 0 element
        statusLive.value = IN_PROGRESS

        viewModelScope.launch {
            repository.getLocations().collect {
                userLocationsLive.value = it
            }
        }
    }

    fun loadAllLocation(list: List<UserLocation>) {
        viewModelScope.launch {
            if (list.isNotEmpty()) {
                for (n in list.indices) {
                    val request: JsonObject = sendRequest(list[n])
                    jsonList.add(request)
                }
                weatherJsonListLive.value = jsonList
                statusLive.value = SUCCESSFUL
            }
        }
    }

    private suspend fun sendRequest(userLocation: UserLocation): JsonObject {
        return suspendCoroutine { continuation ->
            mService.getWeatherOneCall(
                userLocation.lat.toDouble(),
                userLocation.lon.toDouble(),
                "minutely,alerts",
                "metric",
                OPEN_WEATHER_API_KEY
            ).enqueue(object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful)
                    {
                        if (response.body() != null) {
                            continuation.resume(response.body() as JsonObject)
                        } else {
                            continuation.resume(JsonObject())
                        }
                    }
                    else
                    {
                        //continuation.resume(response.body() as JsonObject)
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {

                }

            })
        }
    }

}