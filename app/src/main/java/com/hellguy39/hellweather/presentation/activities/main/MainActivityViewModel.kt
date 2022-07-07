package com.hellguy39.hellweather.presentation.activities.main

import androidx.lifecycle.ViewModel
import com.hellguy39.hellweather.utils.PermissionState
import kotlinx.coroutines.flow.MutableStateFlow

class MainActivityViewModel : ViewModel() {

    private val _permissionState = MutableStateFlow(PermissionState.Unknown)
    val permissionState = _permissionState

    fun updatePermissionState(state: PermissionState) {
        _permissionState.value = state
    }

}