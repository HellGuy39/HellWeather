package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.domain.request_models.CurrentByCityRequest
import com.hellguy39.hellweather.domain.usecase.prefs.units.GetUnitsUseCase
import com.hellguy39.hellweather.domain.usecase.requests.weather.GetCurrentWeatherByCityNameUseCase
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getUnitsUseCase: GetUnitsUseCase,
    private val getCurrentWeatherByCityNameUseCase: GetCurrentWeatherByCityNameUseCase
): ViewModel() {

    private val lang = Locale.getDefault().country
    val currentWeatherLive = MutableLiveData<CurrentWeather>()
    val statusLive = MutableLiveData<String>()
    private val units = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            units.value = getUnitsUseCase.invoke()
        }
    }

    fun getCurrentWeather(cityName: String) {

        if (statusLive.value == IN_PROGRESS)
            return
        else
            statusLive.value = IN_PROGRESS

        viewModelScope.launch(Dispatchers.IO) {
            val model = CurrentByCityRequest(
                cityName = cityName,
                units = getUnits(),
                lang = lang,
                appId = OPEN_WEATHER_API_KEY
            )

            val response = getCurrentWeatherByCityNameUseCase.invoke(model)

            if (response.data != null) {
                currentWeatherLive.value = response.data!!
                statusLive.value = SUCCESSFUL
            } else {
                statusLive.value = ERROR
            }

        }
    }

    fun getUnits(): String {
        if (units.value != null)
            return units.value!!
        else
            return METRIC
    }

}