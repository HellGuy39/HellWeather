package com.hellguy39.hellweather.domain.usecase.prefs.service

import com.hellguy39.hellweather.domain.repository.PrefsRepository
import java.util.prefs.AbstractPreferences

class GetServiceLocationUseCase (private val prefsRepository: PrefsRepository){
    suspend operator fun invoke(): String {
        return prefsRepository.getServiceLocationUseCase()
    }
}