package dk.mustache.corelib.rating_dialog

import android.content.Context
import androidx.annotation.StringRes
import dk.mustache.corelib.R
import dk.mustache.corelib.util.whenNotNull

class AppRatingConfigBuilder(private val context: Context) {

    private var minLaunchCount: Int = 5
    private var resetLaunchCountWhenDialogOpened: Boolean = true

    private var mainDialogTitle: String =
        context.getString(R.string.rating_dialog_main_dialog_title)
    private var mainDialogMessage: String =
        context.getString(R.string.rating_dialog_main_dialog_message)
    private var mainDialogDelay: String =
        context.getString(R.string.rating_dialog_main_dialog_delay)
    private var mainDialogNever: String =
        context.getString(R.string.rating_dialog_main_dialog_never)

    private var minStarsForPositive: Int = 4

    private var disableOnPositiveAction: Boolean = true
    private var disableOnNegativeAction: Boolean = true
    private var positiveRating: ((Int) -> Unit)? = null
    private var negativeRating: ((Int) -> Unit)? = null

    private var onDialogueCompleted: ((RatingButtonType) -> Unit)? = null

    /**
     * Set the text to be used on the primary Rating Dialog. Each parameter
     * allows definition as a string or a string resource with a string
     * definition overriding a string resource. Each of these strings will use
     * a default value if not set.
     *
     * @param title Title of dialog as string
     * @param titleId Title of dialog as string resource
     * @param message Text shown above the dialog buttons, as string. If this
     *     is set to an empty string, the message section will be
     *     hidden/removed.
     * @param messageId Text shown above the dialog buttons, as string resource
     * @param buttonDelay Name of the button which will dismiss the dialog,
     *     allowing it to appear later, as string
     * @param buttonDelayId Name of the button which will dismiss the dialog,
     *     allowing it to appear later, as string resource
     * @param buttonNever Name of the button which will dismiss the dialog
     *     forever, as string
     * @param buttonNeverId Name of the button which will dismiss the dialog
     *     forever, as string resource
     */
    fun setMainDialogStrings(
        title: String? = null, @StringRes titleId: Int? = null,
        message: String? = null, @StringRes messageId: Int? = null,
        buttonDelay: String? = null, @StringRes buttonDelayId: Int? = null,
        buttonNever: String? = null, @StringRes buttonNeverId: Int? = null
    ): AppRatingConfigBuilder {
        mainDialogTitle = getString(title, titleId, R.string.rating_dialog_main_dialog_title)
        mainDialogMessage =
            getString(message, messageId, R.string.rating_dialog_main_dialog_message)
        mainDialogDelay =
            getString(buttonDelay, buttonDelayId, R.string.rating_dialog_main_dialog_delay)
        mainDialogNever =
            getString(buttonNever, buttonNeverId, R.string.rating_dialog_main_dialog_never)
        return this
    }

    /**
     * Decide whether the registered number of app launched are reset to 0,
     * when the rating dialog is opened. If false, the dialog will open every
     * session after the [minLaunchCount] has been reached. If true, the dialog
     * will need another [minLaunchCount] app launches before the dialog shows
     * again.
     *
     * @param reset Defaults to true
     */
    fun setResetLaunchCountWhenDialogOpened(reset: Boolean): AppRatingConfigBuilder {
        resetLaunchCountWhenDialogOpened = reset
        return this
    }

    /**
     * Set the number of times [incrementNumberAppOpens] needs to be called
     * before [showRatingDialogIfLaunchCountSufficient] will launch the
     * rating dialog (or before [shouldRatingDialogOpen] will return true).
     *
     * @param numberLaunches Number of times [incrementNumberAppOpens] needs
     *     calling. If set to 5, the dialog will open on the fifth app launch.
     *     Defaults to 5.
     */
    fun setMinLaunchCount(numberLaunches: Int): AppRatingConfigBuilder {
        minLaunchCount = numberLaunches
        return this
    }

    /**
     * Set the minimum number of stars that count as a positive rating. So, if
     * this is set to 4: 4-5 will count as a positive rating and 1-3 will count
     * as a negative rating.
     *
     * @param numberStars The minimum number of start that count as a positive
     *     rating. Defaults to 4.
     * @return
     */
    fun setMinStarsForPositive(numberStars: Int): AppRatingConfigBuilder {
        minStarsForPositive = numberStars
        return this
    }

    /**
     * Decide whether the dialog should not be shown again if the user chooses
     * a positive rating. On true it won't be shown again. On false it will
     * follow normal "delay" behavior.
     *
     * @param disable Defaults to true.
     */
    fun setDisableOnPositiveAction(disable: Boolean): AppRatingConfigBuilder {
        disableOnPositiveAction = disable
        return this
    }

    /**
     * Decide whether the dialog should not be shown again if the user chooses
     * a negative rating. On true it won't be shown again. On false it will
     * follow normal "delay" behavior.
     *
     * @param disable Defaults to true.
     */
    fun setDisableOnNegativeAction(disable: Boolean): AppRatingConfigBuilder {
        disableOnNegativeAction = disable
        return this
    }

    /**
     * Set a predefined [PositiveRatingActions] action to perform when the user
     * selects a positive rating. If no action is defined, nothing will happen
     * when a positive rating is selected.
     */
    fun setPositiveRating(action: PositiveRatingActions): AppRatingConfigBuilder {
        positiveRating.whenNotNull { throw IllegalStateException("This action would override the already defined PositiveRating") }
        positiveRating = action.action(context)
        return this
    }

    /**
     * Set a predefined [NegativeRatingActions] action to perform when the user
     * selects a negative rating. If no action is defined, nothing will happen
     * when a negative rating is selected.
     */
    fun setNegativeRating(action: NegativeRatingActions): AppRatingConfigBuilder {
        negativeRating.whenNotNull { throw IllegalStateException("This action would override the already defined NegativeRating") }
        negativeRating = action.action(context)
        return this
    }

    /**
     * Define a custom action to perform when the user selects a positive
     * rating. If no action is defined, nothing will happen when a positive
     * rating is selected.
     */
    fun setPositiveRating(func: (rating: Int) -> Unit): AppRatingConfigBuilder {
        positiveRating = func
        return this
    }

    /**
     * Define a custom action to perform when the user selects a negative
     * rating. If no action is defined, nothing will happen when a negative
     * rating is selected.
     */
    fun setNegativeRating(func: (rating: Int) -> Unit): AppRatingConfigBuilder {
        negativeRating = func
        return this
    }

    /** Set callback when an option is selected in the rating dialogue. */
    fun setOnDialogueCompleted(func: (RatingButtonType) -> Unit): AppRatingConfigBuilder {
        onDialogueCompleted = func
        return this
    }

    /** Build a AppRatingConfig object from the given inputs and defaults. */
    fun build(): AppRatingConfig {
        return AppRatingConfig(
            mainDialogTitle,
            mainDialogMessage,
            mainDialogDelay,
            mainDialogNever,
            minLaunchCount,
            resetLaunchCountWhenDialogOpened,
            minStarsForPositive,
            disableOnPositiveAction,
            disableOnNegativeAction,
            positiveRating,
            negativeRating,
            onDialogueCompleted
        )
    }

    private fun getString(
        string: String?,
        @StringRes stringId: Int?,
        @StringRes defStringId: Int
    ): String {
        return if (string != null) {
            string
        } else if (stringId != null) {
            context.getString(stringId)
        } else {
            context.getString(defStringId)
        }
    }

    interface PositiveRatingActions {
        fun action(context: Context): (Int) -> Unit
    }

    interface NegativeRatingActions {
        fun action(context: Context): (Int) -> Unit
    }

    enum class RatingButtonType {
        POSITIVE, NEGATIVE, DELAY, NEVER
    }
}