package com.hellguy39.hellweather.format

import android.content.Context
import com.hellguy39.hellweather.R
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.math.roundToInt

@Singleton
class ValueFormatter @Inject constructor(
    @ApplicationContext private val context: Context
) {

    fun toKilometers(value: Int?): String {
        return if (value != null)
            "${value / 1000} km"
        else
            "0 km"
    }

    fun toMeterPerSecond() {

    }

    fun toTemperature(value: Double?): String {
        return ""
    }

    fun toPercents(value: Double?): String {
        return context.resources.getString(
            R.string.text_value_percents,
            if (value != null) (value * 100).roundToInt() else 0
        )
    }

}