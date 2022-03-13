package com.hellguy39.hellweather.domain.usecase.format

import java.text.SimpleDateFormat
import java.util.*

class FormatTimeWithDayUseCase {
    operator fun invoke(date: Long): String {
        return SimpleDateFormat("E, HH:mm", Locale.getDefault()).format(Date(date * 1000))
    }
}