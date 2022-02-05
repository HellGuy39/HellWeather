package com.hellguy39.hellweather.presentation.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.FIRST_BOOT
import com.hellguy39.hellweather.utils.SERVICE_MODE

private var isFirstBoot = false

class CustomSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val intent = Intent(this, MainActivity::class.java)

        isFirstBoot = sharedPreferences.getBoolean(FIRST_BOOT, true)
        val serviceMode = sharedPreferences.getBoolean(SERVICE_MODE, false)

        if (isFirstBoot)
        {
            intent.putExtra(FIRST_BOOT, true)
        }
        else
        {
            intent.putExtra(FIRST_BOOT, false)
        }

        intent.putExtra(SERVICE_MODE, serviceMode)
        startActivity(intent)
        finish()

    }


}