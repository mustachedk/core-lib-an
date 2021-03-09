package dk.mustache.corelib.utils

import android.text.Html
import android.view.View
import android.widget.EditText
import android.widget.TextView
import androidx.databinding.BindingAdapter

@BindingAdapter("visibilityGoneIfFalse")
fun setVisibilityGoneIfFalse(view: View, visible: Boolean) {
    if (visible) {
        view.visibility = View.VISIBLE
    } else {
        view.visibility = View.GONE
    }
}

@BindingAdapter("htmlText")
fun htmlText(textView: TextView?, htmlText: String?) {
    if (htmlText!=null) {
        textView?.text = Html.fromHtml(htmlText)
    }
}

@BindingAdapter("focusChangedListener")
fun bindFocusChangedListener(view: View?, focusChangedListener: View.OnFocusChangeListener?) {
    view?.onFocusChangeListener = focusChangedListener
}
@BindingAdapter("onKeyListener")
fun bindOnKeyListener(view: EditText?, onKeyListener: View.OnKeyListener?) {
    if (onKeyListener!=null)
        view?.setOnKeyListener(onKeyListener)
}
