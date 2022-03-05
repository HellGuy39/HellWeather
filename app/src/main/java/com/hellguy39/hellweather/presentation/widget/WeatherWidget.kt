package com.hellguy39.hellweather.presentation.widget

import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.Context
import android.widget.RemoteViews
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.domain.usecase.requests.weather.WeatherRequestUseCases
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidget : AppWidgetProvider() {

    @Inject
    lateinit var requestUseCases: WeatherRequestUseCases

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray
    ) {
        for (appWidgetId in appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId)
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
    appWidgetId: Int
) {
    val userLocationParam = getWidgetLocation(context, appWidgetId)



    val views = RemoteViews(context.packageName, R.layout.weather_widget)
    views.setTextViewText(R.id.tvCity, userLocationParam.locationName)


    appWidgetManager.updateAppWidget(appWidgetId, views)
}