package com.hellguy39.hellweather.presentation.activities.main

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.data.api.ApiRepository
import com.hellguy39.hellweather.data.repositories.LocationRepository
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.models.WeatherData
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.domain.GetOneCallWeatherUseCase
import com.hellguy39.hellweather.domain.OneCallResponse
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val repository: LocationRepository,
    //private val mService: ApiService,
    private val apiRepository: ApiRepository,
    private val defSharPrefs: SharedPreferences
): ViewModel() {

    private val getOneCallWeatherUseCase = GetOneCallWeatherUseCase()
    val userLocationsLive = MutableLiveData<List<UserLocation>>()
    val weatherDataListLive = MutableLiveData<List<WeatherData>>()
    val statusLive = MutableLiveData<String>()
    val firstBootLive = MutableLiveData<Boolean>()

    private val weatherDataList: MutableList<WeatherData> = mutableListOf()

    private val lang = Locale.getDefault().country
    private var _units = METRIC
    private var _firstBoot = false

    init {
        statusLive.value = EXPECTATION

        _units = defSharPrefs.getString(PREFS_UNITS, METRIC).toString()
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
            weatherDataList.clear()

            weatherDataListLive.value = weatherDataList
            userLocationsLive.value = listOf()

            viewModelScope.launch(Dispatchers.IO) {
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
            _units = defSharPrefs.getString(PREFS_UNITS, METRIC).toString() //Needs to be updated here

            weatherDataList.clear()

            weatherDataListLive.value = weatherDataList

            if (list.isNotEmpty()) {
                for (n in list.indices) {

                    val model = OneCallResponse(
                        list[n].lat.toDouble(),
                        list[n].lat.toDouble(),
                        "minutely,alerts",
                        _units,
                        lang,
                        OPEN_WEATHER_API_KEY
                    )

                    val request = getOneCallWeatherUseCase.execute(apiRepository, model)

                    if (request.requestResult == ERROR)
                    {
                        statusLive.value = ERROR
                        return@launch
                    }

                    weatherDataList.add(request)
                }

                weatherDataListLive.value = weatherDataList
                statusLive.value = SUCCESSFUL
            }

        }
    }

}