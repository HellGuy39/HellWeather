package com.hellguy39.hellweather.presentation.fragments.add

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.JsonObject
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.data.api.ApiService
import com.hellguy39.hellweather.data.repositories.ApiRepository
import com.hellguy39.hellweather.domain.models.CurrentByCityRequest
import com.hellguy39.hellweather.domain.models.CurrentByCoordinatesRequest
import com.hellguy39.hellweather.domain.usecase.current.GetCurrentWeatherByCityUseCase
import com.hellguy39.hellweather.domain.usecase.current.GetCurrentWeatherByCoordinatesUseCase
import com.hellguy39.hellweather.domain.usecase.current.GetUserLocationByCityUseCase
import com.hellguy39.hellweather.domain.usecase.current.GetUserLocationByCoordinatesUseCase
import com.hellguy39.hellweather.utils.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val getUserLocationByCityUseCase: GetUserLocationByCityUseCase,
    private val getUserLocationByCoordinatesUseCase: GetUserLocationByCoordinatesUseCase
) : ViewModel() {

    private val lang = Locale.getDefault().country
    val userLocationLive = MutableLiveData<UserLocation?>()
    val statusLive = MutableLiveData<String>()

    fun clearData() {
        userLocationLive.value = null
    }

    fun requestWithCityName(cityName: String) {
        if (statusLive.value == IN_PROGRESS)
            return
        else
            statusLive.value = IN_PROGRESS

        viewModelScope.launch(Dispatchers.IO) {

            val model = CurrentByCityRequest(
                cityName,
                METRIC,
                lang,
                OPEN_WEATHER_API_KEY
            )

            val response = getUserLocationByCityUseCase.execute(model)

            withContext(Dispatchers.Main) {
                if (response.requestResult == ERROR) {
                    statusLive.value = ERROR
                    return@withContext
                }

                userLocationLive.value = response
                statusLive.value = SUCCESSFUL
            }
        }
    }

    fun requestWithCoordinates(lat: Double, lon: Double) {
        if (statusLive.value == IN_PROGRESS)
            return
        else
            statusLive.value = IN_PROGRESS

        viewModelScope.launch(Dispatchers.IO) {

            val model = CurrentByCoordinatesRequest(
                lat,
                lon,
                METRIC,
                lang,
                OPEN_WEATHER_API_KEY
            )

            val response = getUserLocationByCoordinatesUseCase.execute(model)


            if (response.requestResult == ERROR) {
                statusLive.value = ERROR
            }
            else
            {
                userLocationLive.value = response
                statusLive.value = SUCCESSFUL
            }
        }
    }


}