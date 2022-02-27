package com.hellguy39.hellweather.domain.usecase.prefs.first_boot

import com.hellguy39.hellweather.domain.repository.PrefsRepository

class GetFirstBootValueUseCase(private val prefsRepository: PrefsRepository) {
    operator fun invoke(): Boolean  {
        return prefsRepository.getFirstBootValue()
    }
}