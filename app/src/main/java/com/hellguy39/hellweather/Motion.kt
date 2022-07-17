package com.hellguy39.hellweather

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.transition.Transition
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialFadeThrough
import com.hellguy39.hellweather.utils.getColorFromAttr
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class Motion @Inject constructor(
    @ApplicationContext private val context: Context
) {
    val fadeThrough = MaterialFadeThrough()

    val containerTransform = MaterialContainerTransform().apply {
        drawingViewId = R.id.nav_host_fragment
        scrimColor = Color.TRANSPARENT
        setAllContainerColors(context.getColorFromAttr(R.attr.colorSurface))
    }

    fun transform(from: View, to: View, elevation: Float = 16f) : Transition {
        return MaterialContainerTransform().apply {
            startView = from
            endView = to
            scrimColor = Color.TRANSPARENT
            endElevation = elevation
            addTarget(to)
        }
    }
}