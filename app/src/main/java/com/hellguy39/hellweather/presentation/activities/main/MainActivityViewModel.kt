package com.hellguy39.hellweather.presentation.activities.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.usecase.locations.GetUserLocationsUseCase
import com.hellguy39.hellweather.domain.models.WeatherData
import com.hellguy39.hellweather.domain.usecase.one_call.GetOneCallWeatherUseCase
import com.hellguy39.hellweather.domain.models.OneCallRequest
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val defSharPrefs: SharedPreferences,
    private val getUserLocationsUseCase: GetUserLocationsUseCase,
    private val getOneCallWeatherUseCase: GetOneCallWeatherUseCase
): ViewModel() {

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
                userLocationsLive.value = getUserLocationsUseCase.execute()
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
                val list = getUserLocationsUseCase.execute()

                withContext(Dispatchers.Main) {
                    userLocationsLive.value = list
                    if (!list.isNullOrEmpty()) {
                        loadAllLocation(list)
                    } else {
                        statusLive.value = EMPTY_LIST
                    }
                }
            }
        }
    }

    fun loadAllLocation(list: List<UserLocation>) {

        weatherDataList.clear()
        weatherDataListLive.value = weatherDataList

        viewModelScope.launch(Dispatchers.IO) {
            _units = defSharPrefs.getString(PREFS_UNITS, METRIC).toString() //Needs to be updated here

            if (list.isNotEmpty()) {
                for (n in list.indices) {

                    val model = OneCallRequest(
                        list[n].lat.toDouble(),
                        list[n].lat.toDouble(),
                        "minutely,alerts",
                        _units,
                        lang,
                        OPEN_WEATHER_API_KEY
                    )

                    val request = getOneCallWeatherUseCase.execute(model)

                    if (request.requestResult == ERROR)
                    {
                        withContext(Dispatchers.Main) {
                            statusLive.value = ERROR
                        }
                        return@launch
                    }

                    weatherDataList.add(request)
                }

                withContext(Dispatchers.Main) {
                    weatherDataListLive.value = weatherDataList
                    statusLive.value = SUCCESSFUL
                }
            }

        }
    }

}