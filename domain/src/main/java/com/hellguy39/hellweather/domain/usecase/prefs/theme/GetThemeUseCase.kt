package com.hellguy39.hellweather.domain.usecase.prefs.theme

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetThemeUseCase(private val prefsRepository: PrefsRepository) {
    operator fun invoke(): String {
        return prefsRepository.getTheme()
    }
}