package dk.mustache.corelib.rating_dialog

data class AppRatingConfig(
    val mainDialogTitle: String,
    val mainDialogMessage: String,
    val mainDialogDelay: String,
    val mainDialogNever: String,
    val minLaunchCount: Int,
    val resetLaunchCountWhenDialogOpened: Boolean,
    val minStarsForPositive: Int,
    val disableOnPositiveAction: Boolean,
    val disableOnNegativeAction: Boolean,
    val positiveRatingFunc: ((Int) -> Unit)?,
    val negativeRatingFunc: ((Int) -> Unit)?,
    val onDialogueCompleted: ((AppRatingConfigBuilder.RatingButtonType) -> Unit)?
)
