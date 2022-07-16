package com.hellguy39.hellweather.presentation.fragments.weather

import android.view.View
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.navigation.fragment.findNavController
import com.google.android.material.transition.MaterialSharedAxis
import com.google.android.material.transition.platform.MaterialElevationScale
import com.hellguy39.hellweather.R

fun WeatherFragment.navigateToLocationFragment() {
    //exitTransition = MaterialSharedAxis(MaterialSharedAxis.Z, true)
    //reenterTransition = MaterialSharedAxis(MaterialSharedAxis.Z, false)
//    val directions = WeatherFragmentDirections.actionWeatherFragmentToLocationFragment()
//    findNavController().navigate(directions)
}

fun WeatherFragment.navigateToDailyWeatherDetailsFragment(itemView: View) {
    exitTransition = MaterialElevationScale(false)
    reenterTransition = MaterialElevationScale(true)
    val directions = WeatherFragmentDirections.actionWeatherFragmentToDailyWeatherDetailsFragment()
    val extras = FragmentNavigatorExtras(itemView to getString(R.string.weather_card_details_transition))
    findNavController().navigate(directions, extras)
}

fun WeatherFragment.navigateToHourlyWeatherDetailsFragment(itemView: View) {
    exitTransition = MaterialElevationScale(false)
    reenterTransition = MaterialElevationScale(true)
    val directions = WeatherFragmentDirections.actionWeatherFragmentToHourlyWeatherDetailsFragment()
    val extras = FragmentNavigatorExtras(itemView to getString(R.string.weather_card_details_transition))
    findNavController().navigate(directions, extras)
}