package com.hellguy39.hellweather.helpers

import android.content.Context
import android.content.pm.PackageManager
import android.location.Location
import android.location.LocationManager
import android.os.Looper
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.hellguy39.hellweather.utils.PermissionState
import dagger.hilt.android.qualifiers.ApplicationContext
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Singleton
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

@Singleton
class LocationManager @Inject constructor (
    @ApplicationContext private val context: Context
) {

    private var fusedLocationProviderClient: FusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(context)

    private val locationRequest = LocationRequest.create().apply {
        interval = TimeUnit.SECONDS.toMillis(15)
        fastestInterval = TimeUnit.SECONDS.toMillis(2)
       // maxWaitTime = TimeUnit.SECONDS.toMillis(2)
        priority = Priority.PRIORITY_HIGH_ACCURACY
    }

    fun checkPermissions() : Enum<PermissionState> {
        return when {
            isPermissionsGranted() -> PermissionState.Denied
            isGeolocationEnabled() -> PermissionState.GPSDisabled
            isPermissionsGranted() || isGeolocationEnabled() -> PermissionState.Granted
            else -> PermissionState.Denied
        }
    }

    suspend fun getLocation(): Location? {
        return suspendCoroutine { continuation ->
            fusedLocationProviderClient.requestLocationUpdates(
                locationRequest,
                object : LocationCallback() {
                    override fun onLocationResult(result: LocationResult) {
                        super.onLocationResult(result)
                        //Log.d("DEBUG", "onLocRes")
                        fusedLocationProviderClient.removeLocationUpdates(this)
                        continuation.resume(result.locations.last())
                    }
                }, Looper.myLooper()
            )
        }
    }

    fun isPermissionsGranted(): Boolean {
        return isFineLocationGranted(context) || isCoarseLocationGranted(context)
    }

    private fun isFineLocationGranted(context: Context): Boolean =
        (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)


    private fun isCoarseLocationGranted(context: Context): Boolean =
        (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                == PackageManager.PERMISSION_GRANTED)

    fun isGeolocationEnabled(): Boolean {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        return locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER) ||
                locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
    }

    companion object LocationExtension {
        val permissions = arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION)
        const val PERMISSION_REQUEST_CODE = 404
    }
}