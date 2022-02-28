package com.hellguy39.hellweather.utils

import android.view.View
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.snackbar.Snackbar
import com.hellguy39.hellweather.domain.utils.Unit
import com.hellguy39.hellweather.presentation.activities.main.MainActivity

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

/*internal fun Snackbar.action(message: String, action: (View) -> Unit) {
    this.setAction(message, action)
}*/

internal fun MaterialToolbar.setToolbarNavigation(toolbar: MaterialToolbar, activity: MainActivity) {
    toolbar.setNavigationOnClickListener {
        activity.openDrawer()
    }
}
