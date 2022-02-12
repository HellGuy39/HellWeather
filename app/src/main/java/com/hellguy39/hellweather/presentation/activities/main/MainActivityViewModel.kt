package com.hellguy39.hellweather.presentation.activities.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.database.pojo.WeatherData
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val mService: ApiService,
    private val defSharPrefs: SharedPreferences
): ViewModel() {

    val userLocationsLive = MutableLiveData<List<UserLocation>>()
    val weatherDataListLive = MutableLiveData<List<WeatherData>>()
    val statusLive = MutableLiveData<String>()
    val firstBootLive = MutableLiveData<Boolean>()

    private val jsonList: MutableList<JsonObject> = mutableListOf()
    private val weatherDataList: MutableList<WeatherData> = mutableListOf()

    private val lang = Locale.getDefault().country
    private var _units = STANDARD
    private var _firstBoot = false

    init {
        statusLive.value = EXPECTATION

        _units = defSharPrefs.getString(PREFS_UNITS, STANDARD).toString()
        _firstBoot = defSharPrefs.getBoolean(PREFS_FIRST_BOOT, true)
        firstBootLive.value = _firstBoot

        viewModelScope.launch {
            if (!_firstBoot) {
                userLocationsLive.value = getLocationsFromRepository()
            }
        }
    }

    fun onRepositoryChanged() {
        _firstBoot = defSharPrefs.getBoolean(PREFS_FIRST_BOOT, false)
        firstBootLive.value = _firstBoot

        if (statusLive.value != IN_PROGRESS) {
            statusLive.value = IN_PROGRESS
            jsonList.clear()
            weatherDataList.clear()

            weatherDataListLive.value = weatherDataList
            userLocationsLive.value = listOf()

            viewModelScope.launch {
                userLocationsLive.value = getLocationsFromRepository()
                val list = userLocationsLive.value
                if (!list.isNullOrEmpty()) {
                    loadAllLocation(list)
                }
                else
                {
                    statusLive.value = EMPTY_LIST
                }
            }
        }
    }

    private suspend fun getLocationsFromRepository() : List<UserLocation> {
        return repository.getLocations().first()
    }

    fun loadAllLocation(list: List<UserLocation>) {
        viewModelScope.launch {
            _units = defSharPrefs.getString(PREFS_UNITS, STANDARD).toString() //Needs to be updated here

            val converter = Converter()
            weatherDataList.clear()
            jsonList.clear()

            weatherDataListLive.value = weatherDataList

            //Log.d("DEBUG", list.toString())

            if (list.isNotEmpty()) {
                //Log.d("DEBUG", "IN IF")
                for (n in list.indices) {
                    val request: JsonObject = sendRequest(list[n])

                    if (request.has("request")) {
                        if (request.asJsonObject.get("request").asString == "failed") {
                            statusLive.value = FAILURE
                            return@launch
                        } else if (request.asJsonObject.get("request").asString == "incorrect obj") {
                            statusLive.value = ERROR
                            return@launch
                        }
                    }

                    jsonList.add(request)
                    val obj : WeatherData = converter.toWeatherObject(request)
                    weatherDataList.add(obj)
                }
                /*for(n in jsonList.indices) {
                    weatherObjList.add(convertToWeatherObject(jsonList[n]))
                }*/
                Log.d("DEBUG", "SUCCESSFUL")
                weatherDataListLive.value = weatherDataList
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
                _units,
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
                            continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                        }
                    }
                    else
                    {
                        continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                        //continuation.resume(response.body() as JsonObject)
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    continuation.resume(JsonObject().apply { addProperty("request", "failed") })
                }

            })
        }
    }

}