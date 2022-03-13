package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.request.CurrentByCityRequest
import com.hellguy39.hellweather.domain.models.weather.CurrentWeather
import com.hellguy39.hellweather.domain.usecase.prefs.lang.LangUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import com.hellguy39.hellweather.domain.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val unitsUseCase: UnitsUseCases,
    private val requestUseCases: WeatherRequestUseCases,
    langUseCases: LangUseCases
): ViewModel() {

    private val lang = langUseCases.getLangUseCase.invoke()
    private val currentWeatherLive = MutableLiveData<CurrentWeather>()
    private val statusLive = MutableLiveData<Enum<State>>()
    private val errorMessage = MutableLiveData<String>()

    fun getCurrentWeather(): LiveData<CurrentWeather> {
        return currentWeatherLive
    }

    fun getStatus(): LiveData<Enum<State>> {
        return statusLive
    }

    fun getErrorMessage(): LiveData<String> {
        return errorMessage
    }

    fun fetchWeather(cityName: String) {
        if (isInProgress())
            return
        else
            statusLive.value = State.Progress

        viewModelScope.launch(Dispatchers.IO) {
            val model = CurrentByCityRequest(
                cityName = cityName,
                units = getUnits(),
                lang = lang,
                appId = OPEN_WEATHER_API_KEY
            )

            val response = requestUseCases.getCurrentWeatherByCityNameUseCase.invoke(model)

            withContext(Dispatchers.Main) {
                if (response.data != null) {
                    currentWeatherLive.value = response.data!!
                    statusLive.value = State.Successful
                } else {
                    errorMessage.value = response.message!!
                    statusLive.value = State.Error
                }
            }

        }
    }

    fun getUnits(): String {
        return unitsUseCase.getUnitsUseCase.invoke()
    }

    private fun isInProgress(): Boolean = statusLive.value == State.Progress

}