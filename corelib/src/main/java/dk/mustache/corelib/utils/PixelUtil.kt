package dk.mustache.corelib.utils

import android.content.res.Resources
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.sp

/**
 * Convert Pixels (px) to Density Pixels (dp)
 */
fun Int.toDp(): Int = (this / Resources.getSystem().displayMetrics.density).toInt()

/**
 * Convert Density Pixels (dp) to Pixels (px)
 */
fun Int.toPx(): Int = (this * Resources.getSystem().displayMetrics.density).toInt()

/**
 * Convert Pixels (px) to Density Pixels (dp)
 */
fun Float.toDp(): Float = (this / Resources.getSystem().displayMetrics.density)

/**
 * Convert Density Pixels (dp) to Pixels (px)
 */
fun Float.toPx(): Float = (this * Resources.getSystem().displayMetrics.density)
