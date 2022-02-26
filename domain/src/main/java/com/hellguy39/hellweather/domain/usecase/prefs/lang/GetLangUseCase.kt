package com.hellguy39.hellweather.domain.usecase.prefs.lang

import java.util.*

class GetLangUseCase {
    operator fun invoke(): String {
        return Locale.getDefault().country
    }
}