package dk.mustache.corelib.utils

import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.net.Uri

object GooglePlay {
    fun startGooglePlayApp(context: Context, packageName: String) {
        val intent = Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=$packageName"))
        var marketFound = false

        // find all applications able to handle our rateIntent
        val otherApps = context.packageManager.queryIntentActivities(intent, 0)
        for (otherApp in otherApps) {
            // look for Google Play application
            if (otherApp.activityInfo.applicationInfo.packageName == "com.android.vending") {

                val otherAppActivity = otherApp.activityInfo
                val componentName = ComponentName(
                    otherAppActivity.applicationInfo.packageName,
                    otherAppActivity.name
                )
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED
                intent.component = componentName
                context.startActivity(intent)
                marketFound = true
                break

            }
        }

        // if GP not present on device, open web browser
        if (!marketFound) {
            val webIntent = Intent(
                Intent.ACTION_VIEW,
                Uri.parse("https://play.google.com/store/apps/details?id=" + context.packageName)
            )
            context.startActivity(webIntent)
        }
    }
}