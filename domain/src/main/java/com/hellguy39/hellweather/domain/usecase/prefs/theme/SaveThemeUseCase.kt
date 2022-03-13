package com.hellguy39.hellweather.domain.usecase.prefs.theme

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveThemeUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(theme: String) {
        prefsRepository.saveTheme(theme)
    }
}