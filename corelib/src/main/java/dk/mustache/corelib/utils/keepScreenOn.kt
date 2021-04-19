package dk.mustache.corelib.utils

import android.app.Activity
import android.view.WindowManager

fun Activity.keepScreenOn(status: Boolean) {
		if (status) {
			window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		} else {
			window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
		}
	}