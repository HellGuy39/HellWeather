package com.hellguy39.hellweather.domain.usecase.prefs.units

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveUnitsUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(units: String) {
        prefsRepository.saveUnits(units)
    }
}