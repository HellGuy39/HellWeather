package com.hellguy39.hellweather.app

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import com.hellguy39.hellweather.core.data.di.dataModule
import com.hellguy39.hellweather.core.network.di.networkModule
import com.hellguy39.hellweather.di.appModule
import com.hellguy39.hellweather.service.LocationService
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

class App: Application() {

    override fun onCreate() {
        super.onCreate()
        initNotificationChannel()
        initKoin()
    }

}

private fun Application.initKoin() {
    startKoin {
        androidLogger()
        androidContext(this@initKoin)
        modules(
            appModule,
            dataModule,
            networkModule
        )
    }
}

private fun Application.initNotificationChannel() {
    val channel = NotificationChannel(
        LocationService.NOTIFICATION_CHANNEL_ID,
        LocationService.NOTIFICATION_CHANNEL_NAME,
        LocationService.NOTIFICATION_CHANNEL_IMPORTANCE
    )
    val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    notificationManager.createNotificationChannel(channel)
}