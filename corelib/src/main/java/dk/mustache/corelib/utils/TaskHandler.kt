package dk.mustache.corelib.utils

import android.os.Looper

object TaskHandler {
    fun doTaskOnMainThread(onStartTask: () -> Unit) {
        if(Looper.getMainLooper().getThread() != Thread.currentThread()) {
            android.os.Handler(Looper.getMainLooper()).post {
                onStartTask()
            }
        } else {
            onStartTask()
        }
    }
}