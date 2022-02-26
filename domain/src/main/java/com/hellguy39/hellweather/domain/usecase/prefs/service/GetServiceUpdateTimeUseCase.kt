package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetServiceUpdateTimeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(): Int {
        return prefsRepository.getServiceUpdateTime()
    }
}