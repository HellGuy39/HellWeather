package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveServiceModeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(value: Boolean) {
        prefsRepository.saveServiceMode(value = value)
    }
}