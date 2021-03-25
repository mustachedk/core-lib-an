package dk.mustache.corelib.utils

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.View
import androidx.core.graphics.drawable.toDrawable
import androidx.databinding.BindingAdapter
import dk.mustache.corelib.MustacheCoreLib

@BindingAdapter("visibilityGoneIfFalse")
fun setVisibilityGoneIfFalse(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("setBackground")
fun setViewBackground(view: View, resource: Drawable) {
    view.background = resource
}

@BindingAdapter("setBackground")
fun setViewBackground(view: View, resource: ColorDrawable) {
    view.background = resource
}
