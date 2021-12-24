package com.hellguy39.hellweather

import android.app.Application
import com.hellguy39.hellweather.di.AppComponent
import com.hellguy39.hellweather.di.DaggerAppComponent

class App: Application() {

    lateinit var appComponent: AppComponent

    override fun onCreate() {
        super.onCreate()
        appComponent = DaggerAppComponent.builder()
            .context(this)
            .build()
    }

}