package com.hellguy39.hellweather.presentation.widget

import android.appwidget.AppWidgetManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import androidx.appcompat.app.AppCompatActivity
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.databinding.WeatherWidgetConfigureBinding
import com.hellguy39.hellweather.domain.models.param.UserLocationParam
import com.hellguy39.hellweather.domain.usecase.local.UserLocationUseCases
import com.hellguy39.hellweather.domain.utils.Prefs
import com.hellguy39.hellweather.domain.utils.Unit
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@AndroidEntryPoint
class WeatherWidgetConfigureActivity : AppCompatActivity() {

    @Inject
    lateinit var userLocationUseCases: UserLocationUseCases

    private var appWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID
    private lateinit var acLocation: AutoCompleteTextView
    private lateinit var acUnits: AutoCompleteTextView

    private var selectedLocation = 0
    private var selectedUnits = Unit.Metric

    private val unitsList = listOf(Unit.Standard.name, Unit.Metric.name, Unit.Imperial.name)

    private lateinit var _binding: WeatherWidgetConfigureBinding
    private var locationList: List<UserLocationParam> = listOf()

    private var onClickListener = View.OnClickListener {

        val _selectedLocation = _binding.acLocation.text.toString()
        var selectedLocationParam = UserLocationParam()

        for (n in locationList.indices) {
            if (locationList[n].locationName == _selectedLocation) {
                selectedLocationParam = locationList[n]
            }
        }

        saveLocation(this, appWidgetId, _binding.acLocation.adapter.getItem(selectedLocation).toString())
        saveUnits(this, appWidgetId, selectedUnits.name)
        saveWidgetLocation(
            this@WeatherWidgetConfigureActivity,
            appWidgetId,
            selectedLocationParam.locationName,
            selectedLocationParam.lat,
            selectedLocationParam.lon
        )

        // It is the responsibility of the configuration activity to update the app widget
        val appWidgetManager = AppWidgetManager.getInstance(this@WeatherWidgetConfigureActivity)
        updateAppWidget(this@WeatherWidgetConfigureActivity, appWidgetManager, appWidgetId)

        // Make sure we pass back the original appWidgetId
        val resultValue = Intent()
        resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, appWidgetId)
        setResult(RESULT_OK, resultValue)
        finish()
    }

    public override fun onCreate(icicle: Bundle?) {
        super.onCreate(icicle)
        setResult(RESULT_CANCELED)
        _binding = WeatherWidgetConfigureBinding.inflate(layoutInflater)
        setContentView(_binding.root)

        _binding.fabNext.setOnClickListener(onClickListener)

        CoroutineScope(Dispatchers.IO).launch {
            val _selectedLocation = getLocation(this@WeatherWidgetConfigureActivity, appWidgetId)
            val selectedUnit = getUnits(this@WeatherWidgetConfigureActivity, appWidgetId)
            val request = userLocationUseCases.getUserLocationListUseCase.invoke()

            val locationNameList = mutableListOf<String>()
            var selectedListPos = 0

            if (request.data == null)
                return@launch

            locationList = request.data!!

            for (n in locationList.indices) {

                if (locationList[n].locationName == _selectedLocation)
                    selectedListPos = n

                locationNameList.add(locationList[n].locationName)
            }

            withContext(Dispatchers.Main) {
                val adapter = ArrayAdapter(
                    this@WeatherWidgetConfigureActivity,
                    R.layout.list_item,
                    locationNameList
                )

                (_binding.acLocation as? AutoCompleteTextView)?.setAdapter(adapter)

                if (_binding.acLocation.adapter.getItem(selectedListPos) != null)
                    _binding.acLocation.setText(
                        _binding.acLocation.adapter.getItem(selectedListPos).toString(), false
                    )

                _binding.acLocation.setOnItemClickListener { _, _, i, _ ->
                    selectedLocation = i
                        //_binding.acLocation.adapter.getItem(i).toString()
                }
            }

        }

        // Find the widget id from the intent.
        val intent = intent
        val extras = intent.extras
        if (extras != null) {
            appWidgetId = extras.getInt(
                AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID
            )
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (appWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish()
            return
        }
    }

    fun saveWidgetLocation(
        context: Context,
        appWidgetId: Int,
        locationName: String,
        lat: Double,
        lon: Double
    ) {
        val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
        prefs.putString(KEY_NAME + appWidgetId, locationName)
        prefs.putString(KEY_LAT + appWidgetId, lat.toString())
        prefs.putString(KEY_LON + appWidgetId, lon.toString())
        prefs.apply()
    }
}

private const val PREFS_NAME = "com.hellguy39.hellweather.presentation.widget.WeatherWidget"
private const val PREF_PREFIX_KEY = "appwidget_"

private const val KEY_LAT = "appwidget_location_lat_"
private const val KEY_LON = "appwidget_location_lon_"
private const val KEY_NAME = "appwidget_location_name_"
private const val KEY_LOCATION = "appwidget_location_"
private const val KEY_UNITS = "appwidget_units_"


internal fun getWidgetLocation(context: Context, appWidgetId: Int): UserLocationParam {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    val lat: Double = prefs.getString(KEY_LAT + appWidgetId, "0.0")?.toDouble() ?: 0.0
    val lon: Double = prefs.getString(KEY_LON + appWidgetId, "0.0")?.toDouble() ?: 0.0
    val name: String = prefs.getString(KEY_NAME + appWidgetId, "N/A") as String
    return UserLocationParam(lat = lat, lon = lon, locationName = name)
}

internal fun deleteWidgetCoordinates(context: Context, appWidgetId: Int) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.remove(KEY_LAT + appWidgetId)
    prefs.remove(KEY_LON + appWidgetId)
    prefs.apply()
}

internal fun saveLocation(context: Context, appWidgetId: Int, location: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(KEY_LOCATION + appWidgetId, location)
    prefs.apply()
}

internal fun saveUnits(context: Context, appWidgetId: Int, units: String) {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0).edit()
    prefs.putString(KEY_UNITS + appWidgetId, units)
    prefs.apply()
}

internal fun getLocation(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(KEY_LOCATION + appWidgetId, Prefs.None.name) as String
}

internal fun getUnits(context: Context, appWidgetId: Int): String {
    val prefs = context.getSharedPreferences(PREFS_NAME, 0)
    return prefs.getString(KEY_UNITS + appWidgetId, Prefs.None.name) as String
}
