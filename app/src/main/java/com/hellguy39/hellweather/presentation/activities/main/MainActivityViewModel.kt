package com.hellguy39.hellweather.presentation.activities.main

import android.location.Location
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.hellguy39.hellweather.utils.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow

//@HiltViewModel
class MainActivityViewModel : ViewModel() {

    private val _permissionState = MutableStateFlow(PermissionState.Unknown)
    val permissionState = _permissionState

    fun updatePermissionState(state: PermissionState) {
        _permissionState.value = state
    }

}