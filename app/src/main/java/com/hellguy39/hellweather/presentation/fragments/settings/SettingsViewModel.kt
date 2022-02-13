package com.hellguy39.hellweather.presentation.fragments.settings

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import com.hellguy39.hellweather.utils.STANDARD
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    private val defSharPrefs: SharedPreferences
) : ViewModel() {

    fun saveUnits(unit: String) {
        viewModelScope.launch {
            defSharPrefs.edit {
                this.putString(PREFS_UNITS, unit)
                commit()
            }
        }
    }

    fun getSavedUnits(): String = defSharPrefs.getString(PREFS_UNITS, METRIC).toString()


}