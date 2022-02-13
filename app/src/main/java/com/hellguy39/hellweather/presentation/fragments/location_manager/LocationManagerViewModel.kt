package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.content.SharedPreferences
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.presentation.adapter.LocationsAdapter
import com.hellguy39.hellweather.repository.database.LocationRepository
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import com.hellguy39.hellweather.utils.STANDARD
import dagger.hilt.android.AndroidEntryPoint
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collect
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