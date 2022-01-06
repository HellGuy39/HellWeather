package com.hellguy39.hellweather.presentation.fragments.home

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

class HomeViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    private fun getUserLocation(): UserLocation {
        val usrLoc = UserLocation()
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context)
        usrLoc.requestName = sharedPreferences.getString("requestName", "N/A").toString()
        usrLoc.locationName = sharedPreferences.getString("locationName", "N/A").toString()
        usrLoc.lat = sharedPreferences.getString("lat", "0").toString()
        usrLoc.lon = sharedPreferences.getString("lon", "0").toString()
        return usrLoc
    }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return HomeViewModel(getUserLocation()) as T
    }
}