package com.hellguy39.hellweather.utils

import android.util.StatsLog.logEvent
import android.view.View
import com.google.android.material.snackbar.Snackbar
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*


internal fun View.shortSnackBar(message: String, action: (Snackbar.() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_SHORT)
    action?.let { snackbar.it() }
    snackbar.show()
}

internal fun View.longSnackBar(message: String, action: (Snackbar.() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_LONG)
    action?.let { snackbar.it() }
    snackbar.show()
}

internal fun View.indefiniteSnackBar(message: String, action: (Snackbar.() -> Unit)? = null) {
    val snackbar = Snackbar.make(this, message, Snackbar.LENGTH_INDEFINITE)
    action?.let { snackbar.it() }
    snackbar.show()
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

/*internal fun Snackbar.action(message: String, action: (View) -> Unit) {
    this.setAction(message, action)
}*/

//internal fun MaterialToolbar.setToolbarNavigation(toolbar: MaterialToolbar, activity: MainActivity) {
//    toolbar.setNavigationOnClickListener {
//        activity.openDrawer()
//    }
//}
