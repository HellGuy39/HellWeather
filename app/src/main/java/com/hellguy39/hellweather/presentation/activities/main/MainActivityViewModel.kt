package com.hellguy39.hellweather.presentation.activities.main

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.UserLocationParam
import com.hellguy39.hellweather.domain.models.WeatherData
import com.hellguy39.hellweather.domain.request_models.OneCallRequest
import com.hellguy39.hellweather.domain.usecase.local.GetUserLocationListUseCase
import com.hellguy39.hellweather.domain.usecase.requests.weather.GetOneCallWeatherUseCase
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
    private val getUserLocationListUseCase: GetUserLocationListUseCase,
    private val getOneCallWeatherUseCase: GetOneCallWeatherUseCase
): ViewModel() {

    val userLocationsLive = MutableLiveData<List<UserLocationParam>>()
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
                val list = getUserLocationListUseCase.invoke()

                if (list.data != null) {
                    userLocationsLive.value = list.data!!
                } else {

                }
            }
        }
    }

    fun getUserLocationsList(): List<UserLocationParam> {
        if (userLocationsLive.value != null) {
            return userLocationsLive.value!!
        }
        else
        {
            return listOf()
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
                val list = getUserLocationListUseCase.invoke()

                if (list.data != null) {
                    loadAllLocation(list.data!!)
                }

                withContext(Dispatchers.Main) {

                    if (list.data != null) {
                        userLocationsLive.value = list.data!!
                    } else {
                        statusLive.value = EMPTY_LIST
                    }

                }
            }
        }
    }

    fun loadAllLocation(list: List<UserLocationParam>) {

        weatherDataList.clear()
        weatherDataListLive.value = weatherDataList

        viewModelScope.launch(Dispatchers.IO) {

            _units = defSharPrefs.getString(PREFS_UNITS, METRIC).toString() //Needs to be updated here

            if (list.isNotEmpty()) {
                for (n in list.indices) {

                    val model = OneCallRequest(
                        list[n].lat,
                        list[n].lat,
                        "minutely,alerts",
                        _units,
                        lang,
                        OPEN_WEATHER_API_KEY
                    )

                    val request = getOneCallWeatherUseCase.invoke(model)

                    if (request.data != null)
                    {
                        weatherDataList.add(request.data!!)
                    }
                    else
                    {
                        withContext(Dispatchers.Main) {
                            statusLive.value = ERROR
                        }
                        return@launch
                    }
                }

                withContext(Dispatchers.Main) {
                    weatherDataListLive.value = weatherDataList
                    statusLive.value = SUCCESSFUL
                }
            }

        }
    }

}