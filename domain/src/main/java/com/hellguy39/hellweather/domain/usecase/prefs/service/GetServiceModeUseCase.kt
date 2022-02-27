package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetServiceModeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(): Boolean {
        return prefsRepository.getServiceMode()
    }
}