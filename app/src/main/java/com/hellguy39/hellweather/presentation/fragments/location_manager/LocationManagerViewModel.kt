package com.hellguy39.hellweather.presentation.fragments.location_manager

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.local.UserLocationUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class LocationManagerViewModel @Inject constructor(
    private val unitsUseCase: UnitsUseCases,
    private val userLocationUseCases: UserLocationUseCases
): ViewModel() {

    fun onDeleteItem(userLocationParam: UserLocationParam) = viewModelScope.launch {
        viewModelScope.launch(Dispatchers.IO) {
            userLocationUseCases.deleteUserLocationUseCase(userLocationParam)
        }
    }

    fun getUnits(): String {
        return unitsUseCase.getUnitsUseCase.invoke()
    }

}