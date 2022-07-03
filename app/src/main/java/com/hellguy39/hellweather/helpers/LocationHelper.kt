package com.hellguy39.hellweather.helpers

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class LocationHelper(private val activity: Activity) {

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(activity)

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(30)
        fastestInterval = TimeUnit.SECONDS.toMillis(15)
        maxWaitTime = TimeUnit.SECONDS.toMillis(2)
        priority = Priority.PRIORITY_BALANCED_POWER_ACCURACY
    }

    suspend fun getLocation(): Location? {
        return suspendCoroutine { continuation ->
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        //Log.d("DEBUG", "onLocRes")
                        continuation.resume(result.locations.last())
                        fusedLocationProviderClient.removeLocationUpdates(this)
                    }
//
//                    override fun onLocationAvailability(p0: LocationAvailability) {
//                        super.onLocationAvailability(p0)
//                        Log.d("DEBUG", "Available: ${p0.isLocationAvailable}")
//                    }
                }, Looper.myLooper()
            )
        }
    }

    fun isPermissionsGranted(): Boolean {
        return isFineLocationGranted(activity) || isCoarseLocationGranted(activity)
    }

    private fun isFineLocationGranted(context: Context): Boolean =
        (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)


    private fun isCoarseLocationGranted(context: Context): Boolean =
        (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

    fun isGeolocationEnabled(): Boolean {
        val locationManager = activity.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object LocationExtension {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        const val PERMISSION_REQUEST_CODE = 404
    }
}