package com.hellguy39.hellweather.service

import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.hellguy39.hellweather.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.koin.android.ext.android.inject

class LocationService: Service() {

    private val serviceScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val locationClient: LocationClient by inject()

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when(intent?.action) {
            ACTION_START -> start()
            ACTION_STOP -> stop()
        }
        return super.onStartCommand(intent, flags, startId)
    }

    private fun start() {

        val notification = NotificationCompat.Builder(this, "")
            .setContentTitle("Tracking location...")
            .setContentText("Location: null")
            .setOngoing(true)

        val notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        locationClient.getLocationUpdated(1000L)
            .catch { e -> e.printStackTrace() }
            .onEach { location ->
                val lat = location.latitude.toString().takeLast(3)
                val lon = location.longitude.toString().takeLast(3)

                val updatedNotification = notification.setContentText("Location: ($lat, $lon)")

                notificationManager.notify(NOTIFICATION_ID, updatedNotification.build())
            }
            .launchIn(serviceScope)

        startForeground(NOTIFICATION_ID, notification.build())
    }

    private fun stop() {
        stopForeground(STOP_FOREGROUND_DETACH)
        stopSelf()
    }

    override fun onDestroy() {
        super.onDestroy()
        serviceScope.cancel()
    }

    companion object {
        const val ACTION_START = "ACTION_START"
        const val ACTION_STOP = "ACTION_STOP"

        const val NOTIFICATION_CHANNEL_ID = "HellWeather location channel"
        const val NOTIFICATION_CHANNEL_NAME = "Location"
        const val NOTIFICATION_ID = 100
        const val NOTIFICATION_CHANNEL_IMPORTANCE = NotificationManager.IMPORTANCE_LOW
    }

}