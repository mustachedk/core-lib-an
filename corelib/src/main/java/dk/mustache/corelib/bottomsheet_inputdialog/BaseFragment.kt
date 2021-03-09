package dk.mustache.corelib.bottomsheet_inputdialog

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager

interface BaseFragment {
    fun refreshFragment() {

    }

    fun hideKeyboard(activity: Activity?) {
        if(activity!=null) {
            val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
            //Find the currently focused view, so we can grab the correct window token from it.
            var view = activity.currentFocus
            //If no view currently has focus, create a new one, just so we can grab a window token from it
            if (view == null) {
                view = View(activity)
            }
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    fun showKeyboard(activity: Activity?, view: View? = null) {
        if (view!=null) {
            val keyboard = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            keyboard?.showSoftInput(view, 0)
        } else {
            val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
            imm!!.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0)
        }
    }
}