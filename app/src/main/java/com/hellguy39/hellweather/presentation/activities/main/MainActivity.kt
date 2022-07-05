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
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.helpers.LocationHelper
import com.hellguy39.hellweather.presentation.activities.view_model.SharedViewModel
import com.hellguy39.hellweather.utils.PermissionState
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
            viewModel.updatePermissionState(PermissionState.Granted)
        } else {
            viewModel.updatePermissionState(PermissionState.Denied)
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

    fun checkPermissions() : Enum<PermissionState> {
        return when {
            !locationHelper.isPermissionsGranted() -> PermissionState.Denied
            !locationHelper.isGeolocationEnabled() -> PermissionState.GPSDisabled
            locationHelper.isPermissionsGranted() || locationHelper.isGeolocationEnabled() -> PermissionState.Granted
            else -> PermissionState.Denied
        }
    }

    fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    fun requestPermission() {
        requestPermissionLauncher.launch(LocationHelper.permissions)
    }
}