package com.hellguy39.hellweather.utils

import android.graphics.Color
import android.view.View
import androidx.transition.Transition
import com.google.android.material.transition.MaterialContainerTransform

internal fun View.transformTo(newView: View, elevation: Float = 16f) : Transition {
    return MaterialContainerTransform().apply {
        startView = this@transformTo
        endView = newView
        scrimColor = Color.TRANSPARENT
        endElevation = elevation
        addTarget(newView)
    }
}
