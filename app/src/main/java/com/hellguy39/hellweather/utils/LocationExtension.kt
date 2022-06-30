package com.hellguy39.hellweather.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import androidx.core.app.ActivityCompat
import com.hellguy39.hellweather.utils.LocationExtension.PERMISSION_REQUEST_CODE
import com.hellguy39.hellweather.utils.LocationExtension.permissions

object LocationExtension {
    val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
    const val PERMISSION_REQUEST_CODE = 404
}

fun isPermissionsGranted(context: Context): Boolean {
    return isFineLocationGranted(context) || isCoarseLocationGranted(context)
}

private fun isFineLocationGranted(context: Context): Boolean =
    (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)


private fun isCoarseLocationGranted(context: Context): Boolean =
    (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
            == PackageManager.PERMISSION_GRANTED)

internal fun isGeolocationEnabled(context: Context): Boolean {
    val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
    return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
            locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
}

//internal fun Activity.requestPermission() {
//    ActivityCompat.requestPermissions(
//        this,
//        permissions,
//        PERMISSION_REQUEST_CODE
//    )
//}
