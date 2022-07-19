package com.hellguy39.hellweather.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.transition.Transition
import com.google.android.material.transition.MaterialContainerTransform
import com.hellguy39.hellweather.R

fun Context.containerTransform() = MaterialContainerTransform().apply {
    drawingViewId = R.id.nav_host_fragment
    scrimColor = Color.TRANSPARENT
    setAllContainerColors(getColorFromAttr(R.attr.colorSurface))
}

internal fun transform(from: View, to: View, elevation: Float = 16f) : Transition {
    return MaterialContainerTransform().apply {
        startView = from
        endView = to
        scrimColor = Color.TRANSPARENT
        endElevation = elevation
        addTarget(to)
    }
}