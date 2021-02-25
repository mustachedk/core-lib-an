package dk.mustache.corelib.utils

import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityGoneIfFalse")
fun setVisibilityGoneIfFalse(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}
