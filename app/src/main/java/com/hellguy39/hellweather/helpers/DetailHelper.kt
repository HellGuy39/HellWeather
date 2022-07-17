package com.hellguy39.hellweather.helpers

import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.presentation.adapter.DetailType

object DetailHelper {
    fun getIcon(detailType: Enum<DetailType>): Int {
        return when(detailType) {
            DetailType.Clouds -> R.drawable.ic_cloudy
            DetailType.DewPoint -> R.drawable.ic_thermostat
            DetailType.Humidity -> R.drawable.ic_humidity
            DetailType.Pressure -> R.drawable.ic_speed
            DetailType.Sunset -> R.drawable.ic_expand_more
            DetailType.Sunrise -> R.drawable.ic_expand_less
            DetailType.UVI -> R.drawable.ic_flare
            DetailType.Wind -> R.drawable.ic_wind
            DetailType.Visibility -> R.drawable.ic_visibility
            else -> R.drawable.ic_unknown
        }
    }

    fun getTitle(detailType: Enum<DetailType>): Int {
        return when(detailType) {
            DetailType.Clouds -> R.string.text_clouds
            DetailType.DewPoint -> R.string.text_dew_point
            DetailType.Humidity -> R.string.text_humidity
            DetailType.Pressure -> R.string.text_pressure
            DetailType.Sunset -> R.string.text_sunset
            DetailType.Sunrise -> R.string.text_sunrise
            DetailType.UVI -> R.string.text_uv_index
            DetailType.Wind -> R.string.text_wind
            DetailType.Visibility -> R.string.text_visibility
            else -> R.string.text_unknown
        }
    }
}