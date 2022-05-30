package dk.mustache.corelib.utils

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

object KeyboardUtil {
    fun hideSoftKeyboard(view: View): Boolean {
        val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        return imm?.hideSoftInputFromWindow(
            view.windowToken,
            0
        ) ?: false
    }

    fun showSoftKeyboard(view: View): Boolean {
        if (view.requestFocus()) {
            val imm = view.context.getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
            return imm?.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT) ?: false
        }
        return false
    }
}