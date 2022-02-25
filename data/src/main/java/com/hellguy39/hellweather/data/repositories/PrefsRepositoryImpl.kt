package com.hellguy39.hellweather.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hellguy39.hellweather.domain.repository.PrefsRepository
import com.hellguy39.hellweather.utils.METRIC
import com.hellguy39.hellweather.utils.PREFS_FIRST_BOOT
import com.hellguy39.hellweather.utils.PREFS_UNITS

class PrefsRepositoryImpl(private val sharedPreferences: SharedPreferences) : PrefsRepository {

    override suspend fun getUnits(): String {
        return sharedPreferences.getString(PREFS_UNITS, METRIC) as String
    }

    override suspend fun saveUnits(units: String) {
        sharedPreferences.edit {
            putString(PREFS_UNITS, units)
            commit()
        }
    }

    override suspend fun getFirstBootValue(): Boolean {
        return sharedPreferences.getBoolean(PREFS_FIRST_BOOT, true)
    }

    override suspend fun saveFirstBootValue(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(PREFS_FIRST_BOOT, value)
            commit()
        }
    }
}