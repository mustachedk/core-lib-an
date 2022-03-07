package dk.mustache.corelib.utils

import android.net.NetworkInfo
import android.Manifest.permission
import android.content.Context
import androidx.annotation.RequiresPermission
import android.net.ConnectivityManager

/**
 * Check if there is any connectivity
 *
 * @param context
 *
 * @return [true] if connected [false] otherwise
 */
@RequiresPermission(permission.ACCESS_NETWORK_STATE)
fun isConnected(context: Context): Boolean {
    return getNetworkInfo(context)?.isConnected?:false
}

/**
 * Get the network info
 *
 * @param context
 *
 * @return Network info
 */
@RequiresPermission(permission.ACCESS_NETWORK_STATE)
private fun getNetworkInfo(context: Context): NetworkInfo? {
    return (context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo!!
}
