package dk.mustache.corelib.rating_dialog

import android.content.Context
import android.content.Intent
import android.net.Uri

/**
 * Open Google Play store on a given app
 *
 * @property storeId The id of the app in Google Play store
 */
class OpenStoreRatingAction(val storeId: String) : AppRatingConfigBuilder.PositiveRatingActions {
    override fun action(context: Context): (Int) -> Unit {
        return {
            val googlePlayStoreIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=$storeId")
            )
            context.startActivity(googlePlayStoreIntent)
            AppRatingManager.neverShowRatingDialogAgain()
        }
    }
}