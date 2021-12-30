package com.hellguy39.hellweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import android.util.Log
import androidx.preference.PreferenceManager


private var isFirstBoot = false

class CustomSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)
        val intent = Intent(this, MainActivity::class.java)

        isFirstBoot = sharedPreferences.getBoolean("first_boot", true)

        if (isFirstBoot)
        {
            intent.putExtra("first_boot", true)
        }
        else
        {
            intent.putExtra("first_boot", false)
        }

        startActivity(intent)
        finish()

    }


}