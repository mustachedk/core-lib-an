package dk.mustache.corelib.utils

import android.R
import android.content.Context
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.TypedValue
import androidx.annotation.DrawableRes
import androidx.core.content.res.ResourcesCompat

object StyleUiHelper {

    /**
     * Gets background drawable from xml layout OR from style OR returns a given default drawable,
     * in decreasing order of priority.
     *
     * @param default drawable to return if no background is set in xml layout or style
     */
    fun getXmlBackgroundValue(
        context: Context,
        attrs: AttributeSet?,
        @DrawableRes default: Int
    ): Drawable? {
        if (attrs == null) {
            return ResourcesCompat.getDrawable(
                context.resources,
                default,
                null
            )
        }

        // Try to get background from layout xml
        val explicitBackground = attrs.getAttributeResourceValue(
            "http://schemas.android.com/apk/res/android",
            "background",
            -1
        )

        // Try to get background from style
        val styleAttributes = context.obtainStyledAttributes(
            attrs.styleAttribute,
            intArrayOf(R.attr.background)
        )
        val styleBackground = styleAttributes.getDrawable(0)

        return if (explicitBackground != -1) {
            ResourcesCompat.getDrawable(
                context.resources,
                explicitBackground,
                null
            )
        } else if (styleBackground != null) {
            styleBackground
        } else {
            ResourcesCompat.getDrawable(
                context.resources,
                default,
                null
            )
        }
    }
}