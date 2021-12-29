package com.hellguy39.hellweather

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.content.SharedPreferences
import androidx.preference.PreferenceManager.getDefaultSharedPreferences

private var isFirstBoot = false

class CustomSplashScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash_screen)

        val sharedPreferences = getDefaultSharedPreferences(this)
        val intent = Intent(this, MainActivity::class.java)
        val edit: SharedPreferences.Editor = sharedPreferences.edit()
        sharedPreferences.getBoolean("first_boot", true)

        if (isFirstBoot)
        {
            intent.putExtra("first_boot", true)
            edit.putBoolean("first_boot", false)
            edit.apply()
            //Something
        }
        else
        {
            intent.putExtra("first_boot", false)
        }

        startActivity(intent)
        finish()

    }


}