package com.hellguy39.hellweather.domain.usecase.prefs.units

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetUnitsUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(): String {
        return prefsRepository.getUnits()
    }
}