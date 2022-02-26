package com.hellguy39.hellweather.presentation.fragments.confirmation

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.local.AddUserLocationUseCase
import com.hellguy39.hellweather.utils.PREFS_FIRST_BOOT
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ConfirmationCityViewModel @Inject constructor(
    private val addUserLocationUseCase: AddUserLocationUseCase,
    private val defSharedPrefs: SharedPreferences
) : ViewModel() {

    fun saveToDatabase(userLocation: UserLocationParam) {
        viewModelScope.launch {
            addUserLocationUseCase.invoke(userLocation)
        }
    }

    fun isFirstBoot(): Boolean {
        return defSharedPrefs.getBoolean(PREFS_FIRST_BOOT, true)
    }

    fun disableFirstBoot() {
        defSharedPrefs.edit().apply {
            putBoolean(PREFS_FIRST_BOOT, false)
        }.apply()
    }

}