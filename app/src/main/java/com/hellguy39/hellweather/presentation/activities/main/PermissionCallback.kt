package com.hellguy39.hellweather.presentation.activities.main

interface PermissionCallback {
    fun onPermissionGranted()
    fun onPermissionDenied()
}