package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.repository.LocationRepository
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.usecase.prefs.units.GetUnitsUseCase
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val getUnitsUseCase: GetUnitsUseCase
): ViewModel() {

    private val units = MutableLiveData<String>()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            units.value = getUnitsUseCase.invoke()
        }
    }

    fun onDeleteItem(userLocation: UserLocation) = viewModelScope.launch {
        //repository.deleteLocation(userLocation)
    }

    fun getUnits(): String {
        if (units.value != null)
            return units.value!!
        else
            return METRIC
    }

}