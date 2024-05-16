package dk.mustache.corelib.rating_dialog

import android.content.Context

object AppRatingManager {
    private var hasNotShownThisSession = true
    lateinit var appRatingConfig: AppRatingConfig
        private set

    /**
     * Setup configuration, using the [AppRatingConfigBuilder]. Prerequisite
     * before using the rest of the manager. If the app uses language
     * localization, this should probably be set after language has been set
     * up.
     */
    fun setAppRatingConfig(appRatingConfig: AppRatingConfig) {
        AppRatingManager.appRatingConfig = appRatingConfig
    }

    /**
     * Show the rating dialog. If [resetLaunchCountWhenDialogOpened] is set to
     * true, this will also set [NumberAppOpens] to 0.
     *
     * @param showNeverButton Whether to show the "Never show again" button in
     *     the dialog. Defaults to false.
     */
    fun showRatingDialog(context: Context, showNeverButton: Boolean = false) {
        if (appRatingConfig.resetLaunchCountWhenDialogOpened) {
            resetNumberAppOpenings()
        }
        RatingDialogHelper(context).showRatingDialog(showNeverButton)
    }

    /**
     * Show the rating dialog if [shouldRatingDialogOpen] returns true. If
     * [resetLaunchCountWhenDialogOpened] is set to true, this will also set
     * [NumberAppOpens] to 0 when the dialog is shown.
     *
     * @param showNeverButton Whether to show the "Never show again" button in
     *     the dialog. Defaults to false.
     * @param onDialogOpened Optional function to run after the dialog is shown
     *     (if it is shown).
     */
    fun showRatingDialogIfLaunchCountSufficient(
        context: Context,
        showNeverButton: Boolean = false,
        onDialogOpened: (() -> Unit)? = null
    ) {
        if (shouldRatingDialogOpen()) {
            if (appRatingConfig.resetLaunchCountWhenDialogOpened) {
                resetNumberAppOpenings()
            }
            RatingDialogHelper(context).showRatingDialog(showNeverButton)
            onDialogOpened?.invoke()
        }
    }

    /**
     * Check whether the rating dialog should show. It shows if
     * [numberAppOpenings] is equal to or greater than [minLaunchCount] AND the
     * rating dialog has not been disabled AND the dialog has not already been
     * shown this session.
     */
    fun shouldRatingDialogOpen(): Boolean {
        return AppRatingSharedPrefs.numberAppOpenings >= appRatingConfig.minLaunchCount
                && AppRatingSharedPrefs.ratingDialogOptedOut.not()
                && hasNotShownThisSession
    }

    /** Check whether the rating dialog has already been shown this session. */
    fun wasShownThisSession(): Boolean {
        return hasNotShownThisSession.not()
    }

    /** Set the rating dialog as already shown this session. */
    fun markAsShownThisSession() {
        hasNotShownThisSession = false
    }

    /** Set the rating dialog as NOT yet shown this session. */
    fun resetShownThisSession() {
        hasNotShownThisSession = true
    }

    /**
     * Disabled the rating dialog, so that [shouldRatingDialogOpen] always
     * returns false
     */
    fun neverShowRatingDialogAgain() {
        AppRatingSharedPrefs.ratingDialogOptedOut = true
    }

    /** Enable the rating dialog, reversing [neverShowRatingDialogAgain] * */
    fun resetNeverShowRatingDialogAgain() {
        AppRatingSharedPrefs.ratingDialogOptedOut = false
    }

    /**
     * Increment [numberAppOpenings] (which will be compared to
     * [minLaunchCount]). Usually done in App or MainActivity, to
     * ensure it is only incremented once each time the app is opened.
     */
    fun incrementNumberAppOpens() {
        AppRatingSharedPrefs.numberAppOpenings++
    }

    /** Set [numberAppOpenings] to 0. */
    fun resetNumberAppOpenings() {
        AppRatingSharedPrefs.numberAppOpenings = 0
    }
}