package com.hellguy39.hellweather.format

import java.text.DateFormat
import java.text.SimpleDateFormat

object DateFormatter {

    fun format(date: Long?, pattern: String): String {
        return if (date != null) {
            val formatter: DateFormat = SimpleDateFormat(pattern)
            formatter.format(date * 1000)
        } else {
            "Unknown time"
        }
    }

    const val WEEK_DAY_AND_HOUR = "EEE, HH:mm"
    const val HOUR = "HH:mm"
    const val WEEK_DAY = "EEE"
    const val DATE_OF_THE_MOUTH_AND_HOUR = "E, MMM d, HH:mm"
}