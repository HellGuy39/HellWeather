package com.hellguy39.hellweather.presentation.fragments.settings

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val unitsUseCases: UnitsUseCases
) : ViewModel() {

    private val unitsLiveData = MutableLiveData<String>()

    init {
        fetchOptions()
    }

    private fun fetchOptions() {
        viewModelScope.launch(Dispatchers.IO) {
            val units = unitsUseCases.getUnitsUseCase.invoke()

            updateUnitsLiveData(units)
        }
    }

    private fun updateUnitsLiveData(units: String) = viewModelScope.launch {
        unitsLiveData.value = units
    }

    fun saveUnits(units: String) = viewModelScope.launch(Dispatchers.IO) {
        unitsUseCases.saveUnitsUseCase.invoke(units = units)

        updateUnitsLiveData(units)
    }

    fun getUnits(): LiveData<String> {
        return unitsLiveData
    }
}