package com.hellguy39.hellweather

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import com.hellguy39.hellweather.databinding.MainActivityBinding

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

        binding.navigationView.setupWithNavController(navController)

        if (firstBoot)
        {
            navController.navigate(HomeFragmentDirections.actionHomeFragmentToWelcomeFragment())
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

