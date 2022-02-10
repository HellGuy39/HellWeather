package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val repository: LocationRepository,
): ViewModel() {

    fun onDeleteItem(userLocation: UserLocation) = viewModelScope.launch {
        repository.deleteLocation(userLocation)
    }
}