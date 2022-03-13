package com.hellguy39.hellweather.domain.usecase.prefs.theme_mode

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetThemeModeUseCase(private val prefsRepository: PrefsRepository) {
    operator fun invoke(): String {
        return prefsRepository.getThemeMode()
    }
}