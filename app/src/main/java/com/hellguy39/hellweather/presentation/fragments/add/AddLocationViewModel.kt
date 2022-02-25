package com.hellguy39.hellweather.presentation.fragments.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.requests.location.GetLocationByCityNameUseCase
import com.hellguy39.hellweather.domain.usecase.requests.location.GetLocationByCoordsUseCase
import com.hellguy39.hellweather.utils.ERROR
import com.hellguy39.hellweather.utils.IN_PROGRESS
import com.hellguy39.hellweather.utils.SUCCESSFUL
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val getLocationByCoordsUseCase: GetLocationByCoordsUseCase,
    private val getLocationByCityNameUseCase: GetLocationByCityNameUseCase
) : ViewModel() {

    val userLocationLive = MutableLiveData<UserLocationParam?>()
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

            val response = getLocationByCityNameUseCase(cityName = cityName)


            if (response.data != null) {
                userLocationLive.value = response.data
                statusLive.value = SUCCESSFUL
            }
            else
            {
                statusLive.value = ERROR
            }
        }
    }

    fun requestWithCoordinates(lat: Double, lon: Double) {
        if (statusLive.value == IN_PROGRESS)
            return
        else
            statusLive.value = IN_PROGRESS

        viewModelScope.launch(Dispatchers.IO) {

            val response = getLocationByCoordsUseCase.invoke(lat =  lat, lon = lon)


            if (response.data != null) {
                userLocationLive.value = response.data
                statusLive.value = SUCCESSFUL
            }
            else
            {
                statusLive.value = ERROR
            }
        }
    }
}