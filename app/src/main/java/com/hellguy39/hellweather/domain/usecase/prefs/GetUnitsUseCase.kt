package com.hellguy39.hellweather.domain.usecase.prefs

import android.content.SharedPreferences
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_UNITS
import javax.inject.Inject

class GetUnitsUseCase @Inject constructor(private val defPrefs: SharedPreferences) {
    fun execute(): String {
        return defPrefs.getString(PREFS_UNITS, METRIC).toString()
    }
}