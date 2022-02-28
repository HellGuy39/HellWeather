package com.hellguy39.hellweather.presentation.activities.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.models.request.OneCallRequest
import com.hellguy39.hellweather.domain.models.weather.WeatherData
import com.hellguy39.hellweather.domain.usecase.local.UserLocationUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.FirstBootValueUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import com.hellguy39.hellweather.domain.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.domain.utils.Unit
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    private val userLocationUseCases: UserLocationUseCases,
    private val unitsUseCases: UnitsUseCases,
    private val firstBootValueUseCases: FirstBootValueUseCases,
    private val weatherRequestUseCases: WeatherRequestUseCases
): ViewModel() {

    private val userLocationsLive = MutableLiveData<List<UserLocationParam>>()
    private val weatherDataListLive = MutableLiveData<List<WeatherData>>()
    private val statusLive = MutableLiveData<Enum<State>>()
    private val errorMessage = MutableLiveData<String>()
    private val firstBootLive = MutableLiveData<Boolean>()

    private val lang = Locale.getDefault().country
    private var units = Unit.Metric.toString()
    private var firstBoot = false

    init {
        statusLive.value = State.Expectation

        units = unitsUseCases.getUnitsUseCase.invoke()
        firstBoot = firstBootValueUseCases.getFirstBootValueUseCase.invoke()

        firstBootLive.value = firstBoot

        viewModelScope.launch(Dispatchers.IO) {

            if (!firstBoot) {
                val list = userLocationUseCases.getUserLocationListUseCase.invoke()

                withContext(Dispatchers.Main) {
                    if (list.data != null) {
                        userLocationsLive.value = list.data!!
                    } else {
                        statusLive.value = State.Empty
                    }
                }
            }
        }
    }

    fun getStatus(): LiveData<Enum<State>>  {
        return statusLive
    }

    fun getUserLocationsList(): LiveData<List<UserLocationParam>> {
        return userLocationsLive
    }

    fun getWeatherDataList(): LiveData<List<WeatherData>> {
        return weatherDataListLive
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun getFirstBootValue(): LiveData<Boolean> {
        return firstBootLive
    }

    fun onRepositoryChanged() {

        updateFirstBootValue()

        if (!isInProgress()) {
            statusLive.value = State.Progress

            clearWeatherDataList()

            userLocationsLive.value = listOf()

            viewModelScope.launch(Dispatchers.IO) {
                val list = userLocationUseCases.getUserLocationListUseCase.invoke()

                if (list.data != null) {
                    fetchWeather(list.data!!)
                }

                withContext(Dispatchers.Main) {

                    if (list.data != null) {
                        userLocationsLive.value = list.data!!
                    } else {
                        statusLive.value = State.Empty
                    }

                }
            }
        }
    }

    fun fetchWeather(locationList: List<UserLocationParam>) {

        viewModelScope.launch(Dispatchers.Main) {
            clearWeatherDataList()
        }

        viewModelScope.launch(Dispatchers.IO) {

            units = unitsUseCases.getUnitsUseCase.invoke() //Needs to be updated here
            val dataList: MutableList<WeatherData> = mutableListOf()

            if (locationList.isNotEmpty()) {
                for (n in locationList.indices) {

                    val model = OneCallRequest(
                        locationList[n].lat,
                        locationList[n].lat,
                        "minutely,alerts",
                        units,
                        lang,
                        OPEN_WEATHER_API_KEY
                    )

                    val request = weatherRequestUseCases.getOneCallWeatherUseCase.invoke(model)

                    if (request.data != null)
                    {
                        // That's need to display location name on Fragment inside ViewPager
                        request.data!!.currentWeather.name = locationList[n].locationName

                        dataList.add(request.data!!)
                    }
                    else
                    {
                        withContext(Dispatchers.Main) {
                            errorMessage.value = request.message!!
                            statusLive.value = State.Error
                        }
                        return@launch
                    }
                }

                withContext(Dispatchers.Main) {
                    weatherDataListLive.value = dataList
                    statusLive.value = State.Successful
                }
            }
        }
    }

    private fun clearWeatherDataList() {
        weatherDataListLive.value = mutableListOf()
    }

    private fun updateFirstBootValue() {
        viewModelScope.launch(Dispatchers.IO) {
            firstBoot = firstBootValueUseCases.getFirstBootValueUseCase.invoke()
            withContext(Dispatchers.Main) {
                firstBootLive.value = firstBoot
            }
        }
    }

    fun isInProgress(): Boolean = statusLive.value == State.Progress

}