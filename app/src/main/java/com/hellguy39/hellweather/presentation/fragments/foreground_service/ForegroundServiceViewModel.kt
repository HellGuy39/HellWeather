package com.hellguy39.hellweather.presentation.fragments.foreground_service

import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.Selector
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForegroundServiceViewModel @Inject constructor(
    private val serviceUseCases: ServiceUseCases
): ViewModel() {

    private val updTimeLiveData = MutableLiveData<Int>()
    private val serviceLocationLiveData = MutableLiveData<String>()
    private val serviceModeLiveData = MutableLiveData<Boolean>()

    init {
        fetchOptions()
    }

    private fun fetchOptions() = viewModelScope.launch(Dispatchers.IO) {
        val updTime = serviceUseCases.getServiceUpdateTimeUseCase.invoke()
        val serviceLocation = serviceUseCases.getServiceLocationUseCase.invoke()
        val serviceMode = serviceUseCases.getServiceModeUseCase.invoke()

        updateServiceModeLiveData(updTime)
        updateServiceLocationLiveData(serviceLocation)
        updateUpdTimeLiveData(serviceMode)
    }

    private fun updateServiceModeLiveData(updTime: Int) = viewModelScope.launch {
        updTimeLiveData.value = updTime
    }

    private fun updateServiceLocationLiveData(serviceLocation: String) = viewModelScope.launch {
        serviceLocationLiveData.value = serviceLocation
    }

    private fun updateUpdTimeLiveData(serviceMode: Boolean) = viewModelScope.launch {
        serviceModeLiveData.value = serviceMode
    }

    fun saveServiceMode(isEnabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceModeUseCase.invoke(value = isEnabled)
        updateUpdTimeLiveData(isEnabled)
    }

    fun saveServiceLocation(locationName: String) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceLocationUseCase.invoke(locationName = locationName)
        updateServiceLocationLiveData(locationName)
    }

    fun saveUpdateTime(minutes: Int) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceUpdateTimeUseCase.invoke(minutes = minutes)
        updateServiceModeLiveData(minutes)
    }

    fun getUpdateTime(): LiveData<Int> {
        return updTimeLiveData
    }

    fun getServiceLocation(): LiveData<String> {
        return serviceLocationLiveData
    }

    fun getServiceMode(): LiveData<Boolean> {
        return serviceModeLiveData
    }
}