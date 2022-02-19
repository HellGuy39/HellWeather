package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.data.repositories.LocationRepository
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val repository: LocationRepository,
    private val defSharedPrefs: SharedPreferences
): ViewModel() {

    fun onDeleteItem(userLocation: UserLocation) = viewModelScope.launch {
        repository.deleteLocation(userLocation)
    }

    fun getUnits(): String = defSharedPrefs.getString(PREFS_UNITS, METRIC).toString()

}