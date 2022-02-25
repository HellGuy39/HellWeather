package com.hellguy39.hellweather.domain.usecase.prefs.first_boot

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class SaveFirstBootValueUseCase(private val prefsRepository: PrefsRepository) {
    suspend operator fun invoke(value: Boolean) {
        prefsRepository.saveFirstBootValue(value)
    }
}