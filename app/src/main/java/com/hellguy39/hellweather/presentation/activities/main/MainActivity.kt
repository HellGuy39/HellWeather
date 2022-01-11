package com.hellguy39.hellweather.presentation.activities.main

import android.R.attr
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.navigation.NavigationBarView
import com.google.android.material.navigation.NavigationView
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.MainActivityBinding
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragmentDirections
import com.hellguy39.hellweather.utils.DISABLE
import com.hellguy39.hellweather.utils.ENABLE
import android.R.attr.radius
import android.view.Window
import android.view.WindowManager
import androidx.activity.viewModels
import androidx.core.content.res.ResourcesCompat
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.ui.NavigationUI

import com.google.android.material.shape.CornerFamily

import com.google.android.material.shape.MaterialShapeDrawable
import com.hellguy39.hellweather.presentation.fragments.home.HomeFragment
import com.hellguy39.hellweather.presentation.fragments.home.HomeViewModel
import com.hellguy39.hellweather.presentation.fragments.home.HomeViewModelFactory
import com.hellguy39.hellweather.presentation.fragments.location_manager.LocationManagerViewModel
import dagger.hilt.android.AndroidEntryPoint

private lateinit var drawerLayout: DrawerLayout
private lateinit var binding: MainActivityBinding

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    //private val locationManagerViewModel : LocationManagerViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val firstBoot: Boolean = intent.getBooleanExtra("first_boot", false)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navigationView.setCheckedItem(R.id.homeFragment)
        NavigationUI.setupWithNavController(binding.navigationView, navController)

        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .build()

        if (firstBoot)
        {
            drawerControl(DISABLE)

            if(navController.currentDestination?.id == R.id.homeFragment)
            {
                navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
            }
        }
    }

    /*fun setSystemBarsColor(action: String)
    {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        if (action == "welcome")
        {
            window.statusBarColor = ResourcesCompat.getColor(resources, R.color.clearSkyNightStart, null)
            window.navigationBarColor = ResourcesCompat.getColor(resources, R.color.clearSkyNightEnd, null)
        }
        else
        {

        }
    }
*/
    fun drawerControl(action: String) {
        when(action) {
            ENABLE -> {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_UNLOCKED)
            }
            DISABLE -> {
                binding.drawerLayout.setDrawerLockMode(DrawerLayout.LOCK_MODE_LOCKED_CLOSED)
            }
        }
    }

    fun openDrawer() = binding.drawerLayout.open()


    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen)
        {
            binding.drawerLayout.close()
        }
        else
        {
            super.onBackPressed()
        }
    }
}

