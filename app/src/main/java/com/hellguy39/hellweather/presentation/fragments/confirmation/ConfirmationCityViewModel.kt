package com.hellguy39.hellweather.presentation.fragments.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCityViewModel @Inject constructor(
    private val repository: LocationRepository
) : ViewModel() {

    fun saveToRoom(userLocation: UserLocation) {
        viewModelScope.launch {
            repository.insertLocation(userLocation)
        }
    }

}