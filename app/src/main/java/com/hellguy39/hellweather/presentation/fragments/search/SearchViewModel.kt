package com.hellguy39.hellweather.presentation.fragments.search

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.CurrentByCityRequest
import com.hellguy39.hellweather.domain.usecase.current.GetCurrentWeatherByCityUseCase
import com.hellguy39.hellweather.domain.models.CurrentWeather
import com.hellguy39.hellweather.domain.usecase.prefs.GetUnitsUseCase
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.IN_PROGRESS
import com.hellguy39.hellweather.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.utils.SUCCESSFUL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val getCurrentWeatherByCityUseCase: GetCurrentWeatherByCityUseCase,
    private val getUnitsUseCase: GetUnitsUseCase
): ViewModel() {

    private val lang = Locale.getDefault().country
    val currentWeatherLive = MutableLiveData<CurrentWeather>()
    val statusLive = MutableLiveData<String>()

    fun getCurrentWeather(cityName: String) {

        if (statusLive.value == IN_PROGRESS)
            return
        else
            statusLive.value = IN_PROGRESS

        viewModelScope.launch(Dispatchers.IO) {
            val model = CurrentByCityRequest(
                cityName,
                getUnits(),
                lang,
                OPEN_WEATHER_API_KEY
            )

            val currentWeather = getCurrentWeatherByCityUseCase.execute(model)

            withContext(Dispatchers.Main) {

                if (currentWeather.requestResult == ERROR)
                {
                    statusLive.value = ERROR
                    return@withContext
                }

                currentWeatherLive.value = currentWeather
                statusLive.value = SUCCESSFUL
            }
        }
    }

    fun getUnits(): String = getUnitsUseCase.execute()

}