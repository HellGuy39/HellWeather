package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveServiceUpdateTimeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(minutes: Int) {
        prefsRepository.saveServiceUpdateTime(minutes = minutes)
    }
}