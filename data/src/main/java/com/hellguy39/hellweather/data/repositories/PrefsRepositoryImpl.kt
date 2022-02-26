package com.hellguy39.hellweather.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hellguy39.hellweather.domain.repository.PrefsRepository
import com.hellguy39.hellweather.utils.*

class PrefsRepositoryImpl(private val sharedPreferences: SharedPreferences) : PrefsRepository {

    override fun getUnits(): String {
        return sharedPreferences.getString(PREFS_UNITS, METRIC) as String
    }

    override suspend fun saveUnits(units: String) {
        sharedPreferences.edit {
            putString(PREFS_UNITS, units)
            commit()
        }
    }

    override fun getFirstBootValue(): Boolean {
        return sharedPreferences.getBoolean(PREFS_FIRST_BOOT, true)
    }

    override suspend fun saveFirstBootValue(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(PREFS_FIRST_BOOT, value)
            commit()
        }
    }

    override fun getServiceMode(): Boolean {
        return sharedPreferences.getBoolean(PREFS_SERVICE_MODE, false)
    }

    override suspend fun saveServiceMode(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(PREFS_SERVICE_MODE, value)
            commit()
        }
    }

    override fun getServiceLocationUseCase(): String {
        return sharedPreferences.getString(PREFS_SERVICE_LOCATION, NONE) as String
    }

    override suspend fun saveServiceLocationUseCase(locationName: String) {
        sharedPreferences.edit {
            putString(PREFS_SERVICE_LOCATION, locationName)
            commit()
        }
    }

    override fun getServiceUpdateTime(): Int {
        return sharedPreferences.getInt(PREFS_SERVICE_UPD_TIME, 3 * 60)
    }

    override suspend fun saveServiceUpdateTime(minutes: Int) {
        sharedPreferences.edit {
            putInt(PREFS_SERVICE_UPD_TIME, minutes)
            commit()
        }
    }
}