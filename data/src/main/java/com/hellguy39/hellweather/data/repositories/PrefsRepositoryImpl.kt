package com.hellguy39.hellweather.data.repositories

import android.content.SharedPreferences
import androidx.core.content.edit
import com.hellguy39.hellweather.domain.repository.PrefsRepository
import com.hellguy39.hellweather.domain.utils.Prefs
import com.hellguy39.hellweather.domain.utils.Unit

class PrefsRepositoryImpl(private val sharedPreferences: SharedPreferences) : PrefsRepository {

    override fun getUnits(): String {
        return sharedPreferences.getString(Prefs.Units.name, Unit.Metric.name) as String
    }

    override suspend fun saveUnits(units: String) {
        sharedPreferences.edit {
            putString(Prefs.Units.name, units)
            commit()
        }
    }

    override fun getFirstBootValue(): Boolean {
        return sharedPreferences.getBoolean(Prefs.FirstBoot.name, true)
    }

    override suspend fun saveFirstBootValue(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(Prefs.FirstBoot.name, value)
            commit()
        }
    }

    override fun getServiceMode(): Boolean {
        return sharedPreferences.getBoolean(Prefs.ServiceMode.name, false)
    }

    override suspend fun saveServiceMode(value: Boolean) {
        sharedPreferences.edit {
            putBoolean(Prefs.ServiceMode.name, value)
            commit()
        }
    }

    override fun getServiceLocationUseCase(): String {
        return sharedPreferences.getString(Prefs.ServiceLocation.name, Prefs.None.name) as String
    }

    override suspend fun saveServiceLocationUseCase(locationName: String) {
        sharedPreferences.edit {
            putString(Prefs.ServiceLocation.name, locationName)
            commit()
        }
    }

    override fun getServiceUpdateTime(): Int {
        return sharedPreferences.getInt(Prefs.ServiceUpdateTime.name, 3 * 60)
    }

    override suspend fun saveServiceUpdateTime(minutes: Int) {
        sharedPreferences.edit {
            putInt(Prefs.ServiceUpdateTime.name, minutes)
            commit()
        }
    }
}