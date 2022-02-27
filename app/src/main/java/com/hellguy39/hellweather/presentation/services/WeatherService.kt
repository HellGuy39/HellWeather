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
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.models.request.OneCallRequest
import com.hellguy39.hellweather.domain.usecase.prefs.service.ServiceUseCases
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import com.hellguy39.hellweather.domain.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.domain.utils.SERVICE_CHANNEL_ID
import com.hellguy39.hellweather.domain.utils.Unit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@AndroidEntryPoint
class WeatherService : Service() {

    @Inject
    lateinit var useCases: ServiceUseCases

    @Inject
    lateinit var requestUseCases: WeatherRequestUseCases

    @Inject
    lateinit var unitsUseCases: UnitsUseCases

    companion object {

        private var isRunning = false

        fun startService(context: Context, userLocationParam: UserLocationParam) {
            val service = Intent(context, WeatherService::class.java)
            service.putExtra("location", userLocationParam)
            ContextCompat.startForegroundService(context, service)
        }

        fun stopService(context: Context) {
            val service = Intent(context, WeatherService::class.java)
            context.stopService(service)
        }

        fun isRunning(): Boolean = isRunning
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {

        isRunning = true

        var units = Unit.Metric.name
        var pauseTime = 60 * 3
        var userLocation = UserLocationParam()
        val lang = Locale.getDefault().country

        CoroutineScope(Dispatchers.IO).launch {
            units = unitsUseCases.getUnitsUseCase.invoke()//intent?.getStringExtra("units")
            pauseTime = useCases.getServiceUpdateTimeUseCase.invoke()//intent?.getLongExtra("pauseTime", 90 * 10000)
            userLocation = intent?.getSerializableExtra("location") as UserLocationParam
            pauseTime *= 10000
        }

        val failedRequestPauseTime: Long = 15 * 1000


        createNotificationsChannel()
        val ntfService = createNotification(resources.getString(R.string.loading), "")

        CoroutineScope(Dispatchers.Default).launch {
            while (isRunning) {

                val model = OneCallRequest(
                    lat = userLocation.lat,
                    lon = userLocation.lon,
                    units = units,
                    lang = lang,
                    appId = OPEN_WEATHER_API_KEY
                )

                val response = requestUseCases.getOneCallWeatherUseCase.invoke(model)

                var notification: Notification
                var contentText: String
                var tittle: String
                val max = resources.getString(R.string.max)
                val min = resources.getString(R.string.min)
                val chanceOfRainText = resources.getString(R.string.chance_of_rain)

                if (response.data == null) {
                    tittle = response.message as String
                    contentText = ""

                    notification = createNotification(tittle, contentText)
                    startForeground(1, notification)
                    delay(failedRequestPauseTime)

                } else {

                    val weatherData = response.data!!

                    contentText = if (units == Unit.Standard.name)
                        weatherData.currentWeather.wDescription + " | " +
                                "$max: ${weatherData.currentWeather.tempMax}K, " +
                                "$min: ${weatherData.currentWeather.tempMin}K" + " | " +
                                "$chanceOfRainText ${weatherData.hourlyWeather[0].pop.toInt()}%"
                    else
                        weatherData.currentWeather.wDescription + " | " +
                                "$max: ${weatherData.currentWeather.tempMax}°, " +
                                "$min: ${weatherData.currentWeather.tempMin}°" + " | " +
                                "$chanceOfRainText ${weatherData.hourlyWeather[0].pop.toInt()}%"

                    tittle = when (units) {
                        Unit.Standard.name -> userLocation.locationName + "  —  ${weatherData.currentWeather.temp}K"
                        Unit.Metric.name -> userLocation.locationName + "  —  ${weatherData.currentWeather.temp}°C"
                        Unit.Imperial.name -> userLocation.locationName + "  —  ${weatherData.currentWeather.temp}°F"
                        else -> userLocation.locationName + "  —  ${weatherData.currentWeather.temp}°"
                    }

                    notification = createNotification(tittle, contentText)
                    startForeground(1, notification)
                    delay(pauseTime.toLong())
                }
            }
        }

        startForeground(1, ntfService)

        return START_STICKY
    }

    override fun onDestroy() {
        super.onDestroy()
        isRunning = false
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

}