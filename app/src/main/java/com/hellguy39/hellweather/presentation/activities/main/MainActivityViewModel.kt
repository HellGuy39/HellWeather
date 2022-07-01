package com.hellguy39.hellweather.presentation.activities.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow


//@HiltViewModel
class MainActivityViewModel : ViewModel() {

    private val _location = MutableLiveData<Location>()

    fun getLocation() : LiveData<Location> = _location

    fun updateLocation(newLocation: Location) {
        _location.value = newLocation
    }

}