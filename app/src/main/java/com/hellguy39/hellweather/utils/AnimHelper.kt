package  com.hellguy39.hellweather.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import android.widget.Button
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

const val DEFAULT_DURATION: Long = 300

class AnimationHelper {

    fun exFabBottomIn(exFab: ExtendedFloatingActionButton) {
        exFab.apply {
            translationY = this.height.toFloat()
            visibility = View.VISIBLE

            animate()
                .translationY(0f)
                .setDuration(DEFAULT_DURATION)
                .setListener(null)
        }
    }

    fun exFabBottomOut(exFab: ExtendedFloatingActionButton) {
        exFab.apply {
            translationY = 0f

            animate()
                .translationY(this.height.toFloat())
                .setDuration(DEFAULT_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        exFab.visibility = View.GONE
                    }
                })
        }
    }

    fun animateBottomIn(navView: BottomNavigationView) {
        navView.apply {
            translationY = this.height.toFloat()
            visibility = View.VISIBLE

            animate()
                .translationY(0f)
                .setDuration(DEFAULT_DURATION)
                .setListener(null)
        }
    }

    fun animateBottomOut(navView: BottomNavigationView) {
        navView.apply {
            translationY = 0f
            animate()
                .translationY(this.height.toFloat())
                .setDuration(DEFAULT_DURATION)
                .setListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator?) {
                        super.onAnimationEnd(animation)
                        navView.visibility = View.GONE
                    }
                })
        }
    }
}