package com.hellguy39.hellweather.presentation.activities.main

import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.os.Looper
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.gms.location.*
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.utils.LocationExtension.permissions
import com.hellguy39.hellweather.utils.isGeolocationEnabled
import com.hellguy39.hellweather.utils.isPermissionsGranted
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var fusedLocationProviderClient: FusedLocationProviderClient
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback
    private lateinit var requestPermissionLauncher: ActivityResultLauncher<Array<out String>>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this)

        locationRequest = LocationRequest.create().apply {
            interval = TimeUnit.SECONDS.toMillis(60)
            fastestInterval = TimeUnit.SECONDS.toMillis(30)
            maxWaitTime = TimeUnit.SECONDS.toMillis(2)
            priority = Priority.PRIORITY_HIGH_ACCURACY
        }

        requestPermissionLauncher = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) {
            if(it[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                it[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true)
            {
                Snackbar.make(binding.root, "Permission granted", Snackbar.LENGTH_LONG).show()
            } else {
                Snackbar.make(binding.root, "Permission denied", Snackbar.LENGTH_LONG).show()
            }
        }

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                super.onLocationResult(locationResult)

                val location = locationResult.lastLocation

                if (location != null) {

                    fusedLocationProviderClient.removeLocationUpdates(this)
                }
            }
        }
    }

    fun checkPermissions() : Boolean {
        when {
            !isPermissionsGranted(this) -> {
                requestPermissionLauncher.launch(permissions)
                return false
            }
            !isGeolocationEnabled(this) -> {
                Snackbar.make(binding.root, "Geolocation is disabled", Snackbar.LENGTH_LONG)
                    .setAction("Open settings") {
                        openLocationSettings()
                    }
                    .show()
                return false
            }
            isPermissionsGranted(this) || isGeolocationEnabled(this) -> {
                return true
            }
            else -> {
                return false
            }
        }
    }

    suspend fun getCurrentLocation(): Location? {
        return null
        //fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
    }

    private fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}