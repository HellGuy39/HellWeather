package com.hellguy39.hellweather.domain.usecase.format

import java.text.SimpleDateFormat
import java.util.*

class FormatTimeUseCase {
    operator fun invoke(date: Long): String {
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(Date(date * 1000))
    }
}