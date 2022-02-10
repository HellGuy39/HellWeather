package com.hellguy39.hellweather.presentation.services

import android.app.Notification
import android.app.Notification.VISIBILITY_SECRET
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.gson.JsonObject
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.repository.database.pojo.UserLocation
import com.hellguy39.hellweather.repository.server.ApiService
import com.hellguy39.hellweather.utils.*
import com.hellguy39.hellweather.utils.Converter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import retrofit2.*
import retrofit2.converter.gson.GsonConverterFactory
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine

class WeatherService : Service() {

    companion object {

        private var isRunning = false

        fun startService(context: Context, userLocation: UserLocation) {
            val service = Intent(context, WeatherService::class.java)
            val units = PreferenceManager.getDefaultSharedPreferences(context).getString(
                PREFS_UNITS, STANDARD)
            val pauseTime = PreferenceManager.getDefaultSharedPreferences(context).getInt(
                PREFS_SERVICE_UPD_TIME, 3 * 60)

            service.putExtra("units", units)
            service.putExtra("pauseTime", pauseTime)
            service.putExtra("location", userLocation)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            val service = Intent(context, WeatherService::class.java)
            context.stopService(service)
        }

        fun isRunning(): Boolean = isRunning
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        //return super.onStartCommand(intent, flags, startId)

        isRunning = true
        val mService = provideApiService()
        val units = intent?.getStringExtra("units")
        var pauseTime = intent?.getLongExtra("pauseTime", 90 * 10000)
        val userLocation = intent?.getParcelableExtra<UserLocation>("location")
        val converter = Converter()

        val failedRequestPauseTime: Long = 15 * 10000

        if (pauseTime != null) {
            pauseTime *= 10000
        }

        createNotificationsChannel()
        val ntfService = createNotification("Loading...", "")

        CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {
                if (userLocation != null) {
                    val weatherJson = request(mService, userLocation, units.toString())
                    var ntfService: Notification
                    var contentText = ""
                    var tittle = ""

                    if (weatherJson.has("request")) {
                        if (weatherJson.asJsonObject.get("request").asString == "failed") {
                            tittle = "Server connection lost"
                            contentText = "We will try again later"
                        } else if (weatherJson.asJsonObject.get("request").asString == "incorrect obj") {
                            tittle = "Error"
                            contentText = "Incorrect server response"
                        } else {
                            tittle = "Something went wrong..."
                            contentText = ""
                        }

                        ntfService = createNotification(tittle, contentText)
                        startForeground(1, ntfService)
                        delay(failedRequestPauseTime)

                    } else {
                        val weatherData = converter.toWeatherObject(weatherJson)

                        contentText = weatherData.currentWeather.wDescription + " | " +
                                "Max.: ${weatherData.currentWeather.tempMax}°, " +
                                "min.: ${weatherData.currentWeather.tempMin}°" + " | " +
                                "Chance of rain ${weatherData.hourlyWeather[0].pop.toInt()}%"
                        tittle =
                            userLocation.locationName + "  —  ${weatherData.currentWeather.temp}°"

                        ntfService = createNotification(tittle, contentText)
                        startForeground(1, ntfService)
                        delay(pauseTime!!)

                    }
                }
                else
                {
                    val ntfService = createNotification("Error", "Location can't be load")
                    startForeground(1, ntfService)
                    delay(pauseTime!!)
                }
            }
        }

        startForeground(1, ntfService)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onBind(p0: Intent?): IBinder? {
        return null
    }

    private fun createNotification(tittle: String, contentText: String): Notification {
        return NotificationCompat.Builder(applicationContext, SERVICE_CHANNEL_ID)
            .setContentTitle(tittle)
            .setContentText(contentText)
            .setSmallIcon(R.drawable.ic_round_cloud_queue_24)
            //.setLargeIcon()
            //.setContentIntent(pendingIntent)
            .setSilent(true)
            .setAutoCancel(true)
            .setShowWhen(false)
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .build()
    }

    private fun createNotificationsChannel() {

        val ntfFrgServ = NotificationChannel(
            SERVICE_CHANNEL_ID,
            "Service",
            NotificationManager.IMPORTANCE_MIN
        )

        ntfFrgServ.description = "Foreground service channel"
        ntfFrgServ.lockscreenVisibility = VISIBILITY_SECRET

        //Создание канала уведомлений
        val manager: NotificationManager = getSystemService(NotificationManager::class.java)
        manager.createNotificationChannel(ntfFrgServ)

    }

    private suspend fun request(mService: ApiService, userLocation: UserLocation, units: String): JsonObject {
        return suspendCoroutine { continuation ->
            mService.getWeatherOneCall(
                userLocation.lat.toDouble(),
                userLocation.lon.toDouble(),
                "minutely,alerts",
                units,
                OPEN_WEATHER_API_KEY
            ).enqueue(object : Callback<JsonObject> {

                override fun onResponse(call: Call<JsonObject>, response: Response<JsonObject>) {

                    if (response.isSuccessful) {
                        if (response.body() != null) {
                            continuation.resume(response.body() as JsonObject)
                        } else {
                            continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                        }
                    } else {
                        continuation.resume(JsonObject().apply { addProperty("request", "incorrect obj") })
                    }

                }

                override fun onFailure(call: Call<JsonObject>, t: Throwable) {
                    continuation.resume(JsonObject().apply { addProperty("request", "failed") })
                }

            })
        }
    }

    private fun provideApiService(): ApiService {
        return retrofitClient().create(ApiService::class.java)
    }

    private fun retrofitClient(): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

}