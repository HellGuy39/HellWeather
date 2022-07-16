package com.hellguy39.hellweather.presentation.activities.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.location.LocationManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var permissionCallback: PermissionCallback

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) {
        if(isPermissionGranted(it)) {
            permissionCallback.onPermissionGranted()
        } else {
            permissionCallback.onPermissionDenied()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        setContentView(binding.root)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        viewModel = ViewModelProvider(this)[MainActivityViewModel::class.java]
        sharedViewModel = ViewModelProvider(this)[SharedViewModel::class.java]

        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController
        NavigationUI.setupWithNavController(binding.navigationView, navController)

    }

    fun openDrawer() {
        binding.root.openDrawer(Gravity.START)
    }

    fun closeDrawer() {
        binding.root.closeDrawer(Gravity.START)
    }

    override fun onBackPressed() {
        if(binding.root.isOpen)
            closeDrawer()
        else
            super.onBackPressed()
    }

    fun openLocationSettings() {
        startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    fun requestPermission(callback: PermissionCallback) {
        permissionCallback = callback
        requestPermissionLauncher.launch(LocationManager.permissions)
    }

    private fun isPermissionGranted(permissionMap: Map<String, Boolean>): Boolean {
        return permissionMap[android.Manifest.permission.ACCESS_FINE_LOCATION] == true ||
                permissionMap[android.Manifest.permission.ACCESS_COARSE_LOCATION] == true
    }
}