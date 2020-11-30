package utils

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.view.KeyCharacterMap
import android.view.KeyEvent
import android.view.ViewConfiguration
import java.lang.Math.abs


object NavigationBar {
    //Detects if the navigation bar is outside the screen area ie built-in
    private fun hasBuiltInNavBar(activity: Activity): Boolean {
        val d = activity.getWindowManager().getDefaultDisplay()
        val dm = DisplayMetrics()
        d.getRealMetrics(dm)
        val realHeight = dm.heightPixels
        val realWidth = dm.widthPixels
        d.getMetrics(dm)
        val displayHeight = dm.heightPixels
        val displayWidth = dm.widthPixels
        return !((realWidth - displayWidth) > 0 || (realHeight - displayHeight) > 0)
    }

    //Measures the height of on-screen and built-in navigation bar
    //On new versions of android (9+) the navigation bar overlaps the layout area
    private fun getHeight(activity: Activity): Int {
        val resources = activity.resources
        val resourceId = resources.getIdentifier("navigation_bar_height", "dimen", "android")
        return if (resourceId > 0) {
            resources.getDimensionPixelSize(resourceId)
        } else 0
    }

    //Returns the height if the navigation bar is on screen 0 if built-in
    fun getHeightOfSoftNavBar(activity: Activity): Int {
        val barHeight = getHeight(activity)
        return when {
            barHeight==0 -> {
                0
            }
            hasBuiltInNavBar(activity) -> {
                0
            }
            else -> {
                barHeight
            }
        }
    }
}