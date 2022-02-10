package com.hellguy39.hellweather.presentation.activities.splash

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.preference.PreferenceManager
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.utils.PREFS_FIRST_BOOT
import com.hellguy39.hellweather.utils.PREFS_SERVICE_MODE

private var isFirstBoot = false

class CustomSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val intent = Intent(this, MainActivity::class.java)

        isFirstBoot = sharedPreferences.getBoolean(PREFS_FIRST_BOOT, true)
        val serviceMode = sharedPreferences.getBoolean(PREFS_SERVICE_MODE, false)

        if (isFirstBoot)
        {
            intent.putExtra(PREFS_FIRST_BOOT, true)
        }
        else
        {
            intent.putExtra(PREFS_FIRST_BOOT, false)
        }

        intent.putExtra(PREFS_SERVICE_MODE, serviceMode)
        startActivity(intent)
        finish()

    }


}