package dk.mustache.corelib.utils

import android.os.Handler
import android.view.View

object ViewUtil {
    val handler = Handler()
    fun getOneClickListener(
        resetDuration: Long,
        callback: View.OnClickListener
    ): OnOneClickListener {
        return object : OnOneClickListener() {
            override fun onOneClick(v: View?) {
                handler.postDelayed({
                    reset()
                }, resetDuration)
                callback.onClick(v)
            }
        }
    }
}

abstract class OnOneClickListener : View.OnClickListener {
    private var clickable = true


    /**
     * Override onOneClick() instead.
     */
    override fun onClick(v: View?) {
        if (clickable) {
            clickable = false
            onOneClick(v)
        }

    }

    /**
     * Override this function to handle clicks.
     * reset() must be called after each click for this function to be called
     * again.
     * @param v
     */
    abstract fun onOneClick(v: View?)

    /**
     * Allows another click.
     */
    fun reset() {
        clickable = true
    }
}


