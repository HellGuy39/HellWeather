package com.hellguy39.hellweather.presentation.activities.main

import com.hellguy39.hellweather.R

class IconHelper {

    fun getByIconId(id: Int?, openWeatherIcon: String?): Int {

        if (id == null || openWeatherIcon == null)
            return R.drawable.ic_unknown

        val isDay = openWeatherIcon.contains("d", true)

        return when(id) {

            // Group 2xx: Thunderstorm
            200 -> R.drawable.ic_thunderstorm
            201 -> R.drawable.ic_thunderstorm
            202	-> R.drawable.ic_thunderstorm
            210	-> R.drawable.ic_thunderstorm
            211	-> R.drawable.ic_thunderstorm
            212	-> R.drawable.ic_thunderstorm
            221	-> R.drawable.ic_thunderstorm
            230	-> R.drawable.ic_thunderstorm
            231	-> R.drawable.ic_thunderstorm
            232 -> R.drawable.ic_thunderstorm

            // Group 3xx: Drizzle
            300 -> R.drawable.ic_rainy
            301 -> R.drawable.ic_rainy
            302 -> R.drawable.ic_rainy
            310 -> R.drawable.ic_rainy
            311 -> R.drawable.ic_rainy
            312 -> R.drawable.ic_rainy
            313 -> R.drawable.ic_rainy
            314 -> R.drawable.ic_rainy
            321 -> R.drawable.ic_rainy

            // Group 5xx: Rain
            500 -> R.drawable.ic_rainy
            501 -> R.drawable.ic_rainy
            502 -> R.drawable.ic_rainy
            503 -> R.drawable.ic_rainy
            504 -> R.drawable.ic_rainy
            511 -> R.drawable.ic_snow // freezing rain
            520 -> R.drawable.ic_rainy
            521 -> R.drawable.ic_rainy
            522 -> R.drawable.ic_rainy
            531 -> R.drawable.ic_rainy

            // Group 6xx: Snow
            600 -> R.drawable.ic_snow
            601 -> R.drawable.ic_snow
            602 -> R.drawable.ic_snow
            611 -> R.drawable.ic_snow
            612 -> R.drawable.ic_snow
            613 -> R.drawable.ic_snow
            615 -> R.drawable.ic_snow
            616 -> R.drawable.ic_snow
            620 -> R.drawable.ic_snow
            621 -> R.drawable.ic_snow
            622 -> R.drawable.ic_snow

            // Group 7xx: Atmosphere
            701 -> R.drawable.ic_foggy
            711 -> R.drawable.ic_foggy
            721 -> R.drawable.ic_foggy
            731 -> R.drawable.ic_foggy
            741 -> R.drawable.ic_foggy
            751 -> R.drawable.ic_foggy
            761 -> R.drawable.ic_foggy
            762 -> R.drawable.ic_foggy
            771 -> R.drawable.ic_foggy
            781 -> R.drawable.ic_tornado

            // Group 800: Clear
            800 -> if (isDay) R.drawable.ic_day_clear else R.drawable.ic_night_clear

            // Group 80x: Clouds
            801 -> if (isDay) R.drawable.ic_day_partly_cloudy else R.drawable.ic_night_partly_cloudy
            802 -> R.drawable.ic_cloudy
            803 -> R.drawable.ic_cloudy
            804 -> R.drawable.ic_cloudy

            else -> R.drawable.ic_unknown
        }

    }

}