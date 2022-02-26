package com.hellguy39.hellweather.presentation.fragments.location_manager

import android.content.SharedPreferences
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.repository.LocationRepository
import com.hellguy39.hellweather.data.enteties.UserLocation
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.local.DeleteUserLocationUseCase
import com.hellguy39.hellweather.domain.usecase.prefs.units.GetUnitsUseCase
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val getUnitsUseCase: GetUnitsUseCase,
    private val deleteUserLocationUseCase: DeleteUserLocationUseCase
): ViewModel() {

    fun onDeleteItem(userLocationParam: UserLocationParam) = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            deleteUserLocationUseCase(userLocationParam)
        }
    }

    fun getUnits(): String {
        return getUnitsUseCase.invoke()
    }

}