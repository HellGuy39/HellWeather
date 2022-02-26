package com.hellguy39.hellweather.presentation.fragments.foreground_service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForegroundServiceViewModel @Inject constructor(
    private val serviceUseCases: ServiceUseCases
): ViewModel() {

    fun saveServiceMode(isEnabled: Boolean) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceModeUseCase.invoke(value = isEnabled)
    }

    fun saveServiceLocation(locationName: String) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceLocationUseCase.invoke(locationName = locationName)
    }

    fun saveUpdateTime(minutes: Int) = viewModelScope.launch(Dispatchers.IO) {
        serviceUseCases.saveServiceUpdateTimeUseCase.invoke(minutes = minutes)
    }

    suspend fun getUpdateTime(): Int = serviceUseCases.getServiceUpdateTimeUseCase.invoke()

    suspend fun getServiceLocation(): String = serviceUseCases.getServiceLocationUseCase.invoke()

    suspend fun getServiceMode(): Boolean = serviceUseCases.getServiceModeUseCase.invoke()

}