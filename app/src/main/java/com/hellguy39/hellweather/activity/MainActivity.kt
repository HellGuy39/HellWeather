package com.hellguy39.hellweather.activity

import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.Composable
import androidx.core.view.WindowCompat
import androidx.lifecycle.lifecycleScope
import com.hellguy39.hellweather.core.model.RequestQuery
import com.hellguy39.hellweather.core.network.NetworkDataSource
import com.hellguy39.hellweather.core.ui.theme.HellWeatherTheme
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent { App() }
    }

    @Composable
    private fun App() {
        HellWeatherTheme {
            WeatherScreen()
        }
    }

}