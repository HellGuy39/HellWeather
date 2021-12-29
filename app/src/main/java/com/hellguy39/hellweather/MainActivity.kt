package com.hellguy39.hellweather

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.NavHostFragment

class MainActivity : AppCompatActivity()
{

    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)

        val firstBoot = intent.extras?.getBoolean("first_boot", false)
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController = navHostFragment.navController

        if (firstBoot as Boolean)
        {
            navController.navigate(R.id.action_homeFragment_to_welcomeFragment)
        }


    }
}

