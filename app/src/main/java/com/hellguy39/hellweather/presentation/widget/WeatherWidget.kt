package com.hellguy39.hellweather.presentation.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.widget.RemoteViews
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.domain.models.request.OneCallRequest
import com.hellguy39.hellweather.domain.usecase.prefs.units.UnitsUseCases
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import com.hellguy39.hellweather.domain.utils.OPEN_WEATHER_API_KEY
import com.hellguy39.hellweather.domain.utils.Unit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.*
import javax.inject.Inject


@AndroidEntryPoint
class WeatherWidget : AppWidgetProvider() {

    @Inject
    lateinit var requestUseCases: WeatherRequestUseCases

    @Inject
    lateinit var unitsUseCases: UnitsUseCases

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId, unitsUseCases, requestUseCases)
        }
    }

    override fun onDeleted(context: Context, appWidgetIds: IntArray) {
        for (appWidgetId in appWidgetIds) {
            deleteWidgetCoordinates(context, appWidgetId)
        }
    }

    override fun onEnabled(context: Context) {
        // Enter relevant functionality for when the first widget is created
    }

    override fun onDisabled(context: Context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

internal fun updateAppWidget(
    context: Context,
    appWidgetManager: AppWidgetManager,
    appWidgetId: Int,
    unitsUseCases: UnitsUseCases,
    requestUseCases: WeatherRequestUseCases
) {
    val userLocationParam = getWidgetLocation(context, appWidgetId)
    val units = unitsUseCases.getUnitsUseCase.invoke()

    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    views.setTextViewText(R.id.tvCity, userLocationParam.locationName)

    val model = OneCallRequest(
        lat = userLocationParam.lat,
        lon = userLocationParam.lon,
        units = units,
        lang = Locale.getDefault().country,
        appId = OPEN_WEATHER_API_KEY
    )

    CoroutineScope(Dispatchers.IO).launch {
        val response = requestUseCases.getOneCallWeatherUseCase.invoke(model)

        withContext(Dispatchers.Main) {
            if (response.data != null) {

                when (units) {
                    Unit.Standard.name -> {
                        views.setTextViewText(R.id.tvTemp, response.data!!.currentWeather.temp + " K")
                    }
                    Unit.Imperial.name -> {
                        views.setTextViewText(R.id.tvTemp, response.data!!.currentWeather.temp + " °F")
                    }
                    Unit.Metric.name -> {
                        views.setTextViewText(R.id.tvTemp, response.data!!.currentWeather.temp + " °C")
                    }
                }

                Glide.with(context)
                    .asBitmap()
                    .load("https://openweathermap.org/img/wn/${response.data!!.currentWeather.icon}@2x.png")
                    .into(object : CustomTarget<Bitmap>(){
                        override fun onResourceReady(
                            resource: Bitmap,
                            transition: Transition<in Bitmap>?
                        ) {
                            views.setImageViewBitmap(R.id.ivWeather, resource)
                            appWidgetManager.updateAppWidget(appWidgetId, views)
                        }

                        override fun onLoadCleared(placeholder: Drawable?) {

                        }

                    })

                appWidgetManager.updateAppWidget(appWidgetId, views)
            } else {

            }
        }
    }
}