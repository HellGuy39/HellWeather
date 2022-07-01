package com.hellguy39.hellweather.presentation.activities.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sharedViewModel: SharedViewModel

    lateinit var locationHelper: LocationHelper

    private val requestPermissionLauncher: ActivityResultLauncher<Array<out String>> = registerForActivityResult(
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        locationHelper = LocationHelper(this)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

    }

    fun checkPermissions() : Boolean {
        when {
            !locationHelper.isPermissionsGranted() -> {
                requestPermissionLauncher.launch(LocationHelper.permissions)
                return false
            }
            !locationHelper.isGeolocationEnabled() -> {
                Snackbar.make(binding.root, "Geolocation is disabled", Snackbar.LENGTH_LONG)
                    .setAction("Open settings") {
                        openLocationSettings()
                    }
                    .show()
                return false
            }
            locationHelper.isPermissionsGranted() || locationHelper.isGeolocationEnabled() -> {
                return true
            }
            else -> {
                return false
            }
        }
    }

    private fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }
}