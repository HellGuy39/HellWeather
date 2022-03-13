package com.hellguy39.hellweather.domain.usecase.prefs.theme_mode

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveThemeModeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(mode: String) {
        prefsRepository.saveThemeMode(mode)
    }
}