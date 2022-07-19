package com.hellguy39.hellweather.utils

import android.content.Context
import android.content.Intent
import android.content.res.Resources.Theme
import android.util.TypedValue
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.domain.model.WeatherForecast
import com.hellguy39.hellweather.format.DateFormatter
import kotlinx.coroutines.flow.MutableStateFlow
import kotlin.math.roundToInt

@ColorInt
internal fun Context.getColorFromAttr(resId: Int): Int {
    val typedValue = TypedValue()
    val theme: Theme = this.theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

inline fun <T> MutableStateFlow<T>.update(function: (T) -> T) {
    while (true) {
        val prevValue = value
        val nextValue = function(prevValue)
        if (compareAndSet(prevValue, nextValue)) {
            return
        }
    }
}

internal fun ImageView.setImageAsync(resId: Int) {
    Glide.with(this.context)
        .load(resId)
        .dontTransform()
        .into(this)
}

internal fun Context.actionSend(forecast: WeatherForecast) {
    val intent = Intent().apply {
        action = Intent.ACTION_SEND
        putExtra(
            Intent.EXTRA_TEXT,
            "Weather in ${forecast.locationInfo?.get(0)?.country}, ${forecast.locationInfo?.get(0)?.name} " +
                    "on ${DateFormatter.format(forecast.oneCallWeather?.currentWeather?.date, DateFormatter.DATE_OF_THE_MOUTH_AND_HOUR)}. " +
                    "Temp: ${resources.getString(R.string.text_value_temp, forecast.oneCallWeather?.currentWeather?.temp?.roundToInt())}, " +
                    "feels like: ${resources.getString(R.string.text_value_temp, forecast.oneCallWeather?.currentWeather?.feelsLike?.roundToInt())}, " +
                    "description: ${forecast.oneCallWeather?.currentWeather?.weather?.get(0)?.description}, " +
                    "wind: ${resources.getString(R.string.text_value_meters_per_sec, forecast.oneCallWeather?.currentWeather?.windSpeed?.roundToInt())}, " +
                    "humidity: ${resources.getString(R.string.text_value_percents, forecast.oneCallWeather?.currentWeather?.humidity)}"
        )
        type = "text/plain"
    }
    startActivity(Intent.createChooser(intent,"Share to:"))
}

internal fun <T> RecyclerView.clearAndUpdateDataSet(adapterDataSet: MutableList<T>, newData: List<T>) {
    val adapter = this.adapter
    // Clear old data
    val previousSize = adapter?.itemCount ?: 0
    adapterDataSet.clear()
    // Set new data
    adapter?.notifyItemRangeRemoved(0, previousSize)
    adapterDataSet.addAll(newData)
    adapter?.notifyItemRangeInserted(0, adapterDataSet.size)
}

internal fun ChipGroup.addTagChips(dataSet: List<String>?) {
    dataSet?.forEach { tag ->
        this.addView(Chip(this.context).apply { text = tag })
    }
}

fun EditText.onSubmit(func: () -> Unit) {
    setOnEditorActionListener { _, actionId, _ ->
        if (actionId == EditorInfo.IME_ACTION_DONE) {
            func()
        }
        true
    }
}