package com.hellguy39.hellweather.utils

import android.content.res.Resources.Theme
import android.media.Image
import android.util.TypedValue
import android.view.LayoutInflater
import android.widget.ImageView
import androidx.annotation.ColorInt
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.chip.Chip
import com.google.android.material.chip.ChipGroup
import com.hellguy39.hellweather.R
import com.hellguy39.hellweather.helpers.IconHelper
import kotlinx.coroutines.flow.MutableStateFlow
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


@ColorInt
internal fun Fragment.getColorFromAttr(resId: Int): Int {
    val typedValue = TypedValue()
    val theme: Theme = this.requireContext().theme
    theme.resolveAttribute(resId, typedValue, true)
    return typedValue.data
}

internal fun Long.formatAsDayWithTime(): String {
    val date = Date(this * 1000)
    val formatter: DateFormat = SimpleDateFormat("EEE, HH:mm")
    return formatter.format(date)
}

internal fun Long.formatAsHour(): String {
    val date = Date(this * 1000)
    val formatter: DateFormat = SimpleDateFormat("HH:mm")
    return formatter.format(date)
}

internal fun Long.formatAsDay(): String {
    val date = Date(this * 1000)
    val formatter: DateFormat = SimpleDateFormat("EEE")
    return formatter.format(date)
}

internal fun Long.formatAsTitleDate(): String {
    val date = Date(this * 1000)
    val formatter: DateFormat = SimpleDateFormat("E, MMM d, HH:mm")
    return formatter.format(date)
}

internal fun Double?.toPercents(): Int {
    return if (this == null)
        0
    else
        (this * 100).toInt()
}

internal fun Int?.toKilometers(): String {
    return (this?.div(1000)).toString() + " km"
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
        .into(this)
}

internal fun <T> RecyclerView.updateAndClearRecycler(adapterDataSet: MutableList<T>, newData: List<T>) {
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