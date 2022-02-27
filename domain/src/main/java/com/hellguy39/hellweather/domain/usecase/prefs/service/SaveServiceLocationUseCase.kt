package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository
import java.util.prefs.AbstractPreferences

class SaveServiceLocationUseCase (private val prefsRepository: PrefsRepository){
    suspend operator fun invoke(locationName: String) {
        prefsRepository.saveServiceLocationUseCase(locationName = locationName)
    }
}