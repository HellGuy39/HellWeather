package com.hellguy39.hellweather.presentation.fragments.add

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.requests.location.LocationRequestUseCases
import com.hellguy39.hellweather.utils.State
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class AddLocationViewModel @Inject constructor(
    private val locationRequestUseCases: LocationRequestUseCases
) : ViewModel() {

    val userLocationLive = MutableLiveData<UserLocationParam?>()
    val statusLive = MutableLiveData<Enum<State>>()
    val errorMessage = MutableLiveData<String>()

    fun clearData() {
        userLocationLive.value = null
    }

    fun requestWithCityName(cityName: String) {

        if (isInProgress())
            return
        else
            statusLive.value = State.Progress

        viewModelScope.launch(Dispatchers.IO) {

            val response = locationRequestUseCases.getLocationByCityNameUseCase(cityName = cityName)


            withContext(Dispatchers.Main) {
                if (response.data != null) {
                    userLocationLive.value = response.data
                    statusLive.value = State.Successful

                } else {
                    errorMessage.value = response.message!!
                    statusLive.value = State.Error
                }
            }
        }
    }

    fun requestWithCoordinates(lat: Double, lon: Double) {

        if (isInProgress())
            return
        else
            statusLive.value = State.Progress

        viewModelScope.launch(Dispatchers.IO) {

            val response = locationRequestUseCases.getLocationByCoordsUseCase.invoke(lat =  lat, lon = lon)

            withContext(Dispatchers.Main) {
                if (response.data != null) {
                    userLocationLive.value = response.data
                    statusLive.value = State.Successful
                }
                else
                {
                    errorMessage.value = response.message!!
                    statusLive.value = State.Error
                }
            }
        }
    }

    private fun isInProgress(): Boolean = statusLive.value == State.Progress

}