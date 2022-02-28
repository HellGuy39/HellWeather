package com.hellguy39.hellweather.presentation.fragments.confirmation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.local.UserLocationUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.first_boot.FirstBootValueUseCases
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCityViewModel @Inject constructor(
    private val userLocationUseCases: UserLocationUseCases,
    private val firstBootValueUseCases: FirstBootValueUseCases
) : ViewModel() {

    fun saveToDatabase(userLocation: UserLocationParam) {
        viewModelScope.launch(Dispatchers.IO) {
            userLocationUseCases.addUserLocationUseCase.invoke(userLocation)
        }
    }

    fun isFirstBoot(): Boolean {
        return firstBootValueUseCases.getFirstBootValueUseCase.invoke()
    }

    fun disableFirstBoot() {
        viewModelScope.launch(Dispatchers.IO) {
            firstBootValueUseCases.saveFirstBootValueUseCase.invoke(false)
        }
    }

}