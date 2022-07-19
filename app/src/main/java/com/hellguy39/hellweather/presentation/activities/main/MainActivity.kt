package com.hellguy39.hellweather.presentation.activities.main

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.Gravity
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import androidx.transition.TransitionManager
import com.google.android.material.transition.MaterialSharedAxis
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.HeaderNavigationViewBinding
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.format.DateFormatter
import com.hellguy39.hellweather.helpers.IconHelper
import com.hellguy39.hellweather.location.LocationManager
import com.hellguy39.hellweather.utils.setImageAsync
import dagger.hilt.android.AndroidEntryPoint
import kotlin.math.roundToInt

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: MainActivityBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

    private lateinit var viewModel: MainActivityViewModel
    private lateinit var sharedViewModel: SharedViewModel

    private lateinit var permissionCallback: PermissionCallback

    private lateinit var headerView: View
    private lateinit var headerBinding: HeaderNavigationViewBinding
    private var isLocationInfoHeaderVisible = false

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

        headerView = binding.navigationView.inflateHeaderView(R.layout.header_navigation_view)
        headerBinding = HeaderNavigationViewBinding.bind(headerView)
    }

    fun openDrawer() {
        binding.root.openDrawer(Gravity.START)
    }

    fun closeDrawer() {
        binding.root.closeDrawer(Gravity.START)
    }

    fun updateDrawerData(data: WeatherForecast) {
        headerBinding.run {
            data.oneCallWeather?.currentWeather?.let { currentWeather ->
                tvTemp.text = currentWeather.temp?.roundToInt().toString()
                tvTempFeelsLike.text = resources.getString(
                    R.string.text_temp_feels_like,
                    currentWeather.feelsLike?.roundToInt()
                )
                tvWeatherDescription.text = currentWeather
                    .weather?.get(0)?.description?.replaceFirstChar(Char::titlecase)
                ivIcon.setImageAsync(IconHelper.getByIconId(currentWeather.weather?.get(0)))
            }
            data.locationInfo?.let {
                it[0].let { info ->
                    chipCity.text = getString(R.string.text_country_and_city_name, info.country, info.name)
                    tvLat.text = getString(R.string.text_lat, info.lat?.toFloat())
                    tvLon.text = getString(R.string.text_lon, info.lon?.toFloat())
                    tvCountry.text = getString(R.string.text_country, info.country)
                    tvState.text = getString(R.string.text_state, info.state)
                    tvName.text = getString(R.string.text_city_name, info.name)
                }
            }

            chipCity.setOnClickListener {
                val sharedAxis = MaterialSharedAxis(MaterialSharedAxis.X, true)
                TransitionManager.beginDelayedTransition(container, sharedAxis)

                if (isLocationInfoHeaderVisible) {
                    layoutWeather.visibility = View.VISIBLE
                    layoutLocationInfo.visibility = View.GONE
                    isLocationInfoHeaderVisible = false
                } else {
                    layoutWeather.visibility = View.GONE
                    layoutLocationInfo.visibility = View.VISIBLE
                    isLocationInfoHeaderVisible = true
                }
            }
        }
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