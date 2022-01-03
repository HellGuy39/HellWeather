package com.hellguy39.hellweather.presentation.fragments.add

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

class AddCityViewModelFactory(
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return AddCityViewModel() as T
    }
}