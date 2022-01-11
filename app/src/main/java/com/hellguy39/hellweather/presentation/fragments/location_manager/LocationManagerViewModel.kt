package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.HourlyWeather
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val repository: LocationRepository
): ViewModel() {

    var userLocations = MutableLiveData<List<UserLocation>>()

    init {
        viewModelScope.launch {
            repository.getLocations().collect {
                userLocations.value = it
            }
        }
    }

    fun onEvent(event: LocationListEvent) {
        when(event) {
            is LocationListEvent.OnDeleteLocation -> {

            }
            is LocationListEvent.OnAddItemClick -> {

            }
            is LocationListEvent.OnDoneChange -> {

            }
            is LocationListEvent.OnItemClick -> {

            }
            is LocationListEvent.OnUndoDeleteClick -> {

            }
        }
    }

}