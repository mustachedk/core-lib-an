package dk.mustache.corelib.rating_dialog

import android.content.Context
import android.content.SharedPreferences
import dk.mustache.corelib.MustacheCoreLib.context

object AppRatingSharedPrefs {
    private const val KEY_RATING_DIALOG_OPTED_OUT = "APP_RATING_DIALOG_OPTED_OUT"
    private const val KEY_NUMBER_APP_OPENINGS = "APP_RATING_NUMBER_APP_OPENINGS"

    private var _instance: SharedPreferences? = null
    private val instance: SharedPreferences
        get() = _instance ?: initialize()

    private fun initialize(): SharedPreferences {
        _instance = context.getSharedPreferences(
            "corelib_myucph_rating_dialog_preferences",
            Context.MODE_PRIVATE
        )
        return _instance!!
    }

    var ratingDialogOptedOut: Boolean
        get() = instance.getBoolean(KEY_RATING_DIALOG_OPTED_OUT, false)
        set(value) = instance.edit().putBoolean(KEY_RATING_DIALOG_OPTED_OUT, value).apply()

    var numberAppOpenings: Int
        get() = instance.getInt(KEY_NUMBER_APP_OPENINGS, 0)
        set(value) = instance.edit().putInt(KEY_NUMBER_APP_OPENINGS, value).apply()
}