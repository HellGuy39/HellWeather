package com.hellguy39.hellweather.utils

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.view.View
import androidx.annotation.StringRes
import androidx.core.content.ContextCompat
import androidx.preference.PreferenceManager
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.presentation.activities.main.MainActivity
import com.hellguy39.hellweather.repository.database.pojo.UserLocation

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

internal fun Snackbar.action(message: String, action: (View) -> Unit) {
    this.setAction(message, action)
}
