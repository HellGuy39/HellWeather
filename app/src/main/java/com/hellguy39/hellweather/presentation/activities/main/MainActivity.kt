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

import com.google.android.material.shape.CornerFamily

import com.google.android.material.shape.MaterialShapeDrawable




private lateinit var drawerLayout: DrawerLayout
private lateinit var binding: MainActivityBinding

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = MainActivityBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val firstBoot: Boolean = intent.getBooleanExtra("first_boot", false)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        binding.navigationView.setCheckedItem(R.id.nav_home)
        //binding.navigationView.setupWithNavController(navController)
        binding.navigationView.setNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.nav_settings -> {
                    Log.d("DEBUG", "SETTINGS")
                    navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                }
                R.id.nav_home -> {
                    Log.d("DEBUG", "HOME")
                    /*navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                    binding.navigationView.setCheckedItem(R.id.nav_settings)*/
                }
                R.id.nav_quick_weather -> {
                    Log.d("DEBUG", "QUICK")
                    /*navController.navigate(R.id.action_homeFragment_to_settingsFragment)
                    binding.navigationView.setCheckedItem(R.id.nav_settings)*/
                }
            }
            binding.drawerLayout.close()
            true
        }

        val navViewBackground:MaterialShapeDrawable = binding.navigationView.background as MaterialShapeDrawable
        navViewBackground.shapeAppearanceModel = navViewBackground.shapeAppearanceModel
            .toBuilder()
            .setTopRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .setBottomRightCorner(CornerFamily.ROUNDED, resources.getDimensionPixelSize(R.dimen.drawer_corner).toFloat())
            .build()

        if (firstBoot)
        {
            drawerControl(DISABLE)
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
        }
    }

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

    fun openDrawer() {
        binding.drawerLayout.open()
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isOpen) {
            binding.drawerLayout.close()
        }
        else
        {
            super.onBackPressed()
        }
    }
}

