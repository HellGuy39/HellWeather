package com.hellguy39.hellweather.presentation.fragments.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val unitsUseCases: UnitsUseCases
) : ViewModel() {

    fun saveUnits(unit: String) {
        viewModelScope.launch {
            unitsUseCases.saveUnitsUseCase.invoke(units = unit)
        }
    }

    fun getSavedUnits(): String = unitsUseCases.getUnitsUseCase.invoke()


}