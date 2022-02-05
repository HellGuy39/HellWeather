package com.hellguy39.hellweather.presentation.fragments.foreground_service

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hellguy39.hellweather.utils.ENABLE
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
                putBoolean("serviceMode", isEnabled)
            }.apply()
        }
    }

}