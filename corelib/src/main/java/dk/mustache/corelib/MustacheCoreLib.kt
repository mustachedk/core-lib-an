package dk.mustache.corelib

import android.app.Application
import android.content.Context

object MustacheCoreLib {
    lateinit var context: Context

    fun init(appContext: Application) {
        context = appContext
    }

    fun getContextCheckInit(): Context {
        if (::context.isInitialized) {
            return context
        } else {
            throw Exception("MustacheCoreLib must be initialized in Application")
        }
    }
}