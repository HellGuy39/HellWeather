package com.hellguy39.hellweather.presentation.fragments.foreground_service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.utils.NONE
import com.hellguy39.hellweather.utils.PREFS_SERVICE_LOCATION
import com.hellguy39.hellweather.utils.PREFS_SERVICE_MODE
import com.hellguy39.hellweather.utils.PREFS_SERVICE_UPD_TIME
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ForegroundServiceViewModel @Inject constructor(
    private val defSharedPrefs: SharedPreferences
): ViewModel() {

    fun saveServiceMode(isEnabled: Boolean) {
        viewModelScope.launch {
            defSharedPrefs.edit().apply {
                putBoolean(PREFS_SERVICE_MODE, isEnabled)
            }.apply()
        }
    }

    fun saveServiceLocation(locationName: String) {
        viewModelScope.launch {
            defSharedPrefs.edit().apply {
                putString(PREFS_SERVICE_LOCATION, locationName)
            }.apply()
        }
    }

    fun saveUpdateTime(minutes: Int) {
        viewModelScope.launch {
            defSharedPrefs.edit().apply {
                putInt(PREFS_SERVICE_UPD_TIME, minutes)
            }.apply()
        }
    }

    fun getUpdateTime(): Int = defSharedPrefs.getInt(PREFS_SERVICE_UPD_TIME, 3 * 60)

    fun getServiceLocation(): String = defSharedPrefs.getString(PREFS_SERVICE_LOCATION, NONE).toString()

}