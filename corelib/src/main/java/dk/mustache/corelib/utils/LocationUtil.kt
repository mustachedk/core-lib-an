package dk.mustache.corelib.utils

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.annotation.SuppressLint
import android.app.Activity
import android.location.Location
import android.os.Looper
import android.util.Log
import com.google.android.gms.location.*

/**
 * Usage of the LocationUtil:
 * 1) Create an instance of the LocationUtil, providing your current [activity] as param
 * 2) Call registerLocationListener() with a LocationChangedCallback
 * 3) Call startLocationUpdates()
 */
class LocationUtil(private val activity: Activity) {
    private lateinit var locationRequest: LocationRequest
    private lateinit var locationCallback: LocationCallback

    private var isReceivingLocationUpdates: Boolean = false

    companion object {
        private const val TAG = "LocationUtil"
        private const val LOCATION_UPDATE_INTERVAL: Long = 1000
        private const val LOCATION_FASTEST_UPDATE_INTERVAL: Long = 500
    }

    interface LocationChangedCallback {
        fun locationChanged(location: Location)
    }

    /**
     * Register the location listener. This sets up the foundation to be able to call startLocationUpdates
     */
    fun registerLocationListener(locationListener: LocationChangedCallback?) {
        locationRequest = LocationRequest.create().run {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = LOCATION_UPDATE_INTERVAL
            fastestInterval = LOCATION_FASTEST_UPDATE_INTERVAL
            this
        }

        val builder = LocationSettingsRequest.Builder()
        builder.addLocationRequest(locationRequest)
        val locationSettingsRequest = builder.build()

        val settingsClient = LocationServices.getSettingsClient(activity)
        settingsClient.checkLocationSettings(locationSettingsRequest)

        locationCallback = object : LocationCallback() {
            override fun onLocationResult(locationResult: LocationResult) {
                locationListener?.locationChanged(locationResult.lastLocation)
            }
        }
    }

    /**
     * Request location updates, if we're not already receiving them
     * Ensure you have requested permissions fo [ACCESS_FINE_LOCATION]
     *
     * NOTE: Missing Permission suppressed, because we're already asking about hasPermission()
     */
    @SuppressLint("MissingPermission")
    fun startLocationUpdates() {
        if(hasPermission(activity, ACCESS_FINE_LOCATION) && !isReceivingLocationUpdates) {
            if(::locationRequest.isInitialized && ::locationCallback.isInitialized) {
                LocationServices.getFusedLocationProviderClient(activity)
                    .requestLocationUpdates(locationRequest, locationCallback, Looper.myLooper())
                    .addOnCompleteListener { isReceivingLocationUpdates = true }
            } else {
                Log.e(TAG, "locationRequest OR locationCallback not initialized")
            }
        }
    }

    /**
     * Remove location updates, if we're receiving them
     */
    fun stopLocationUpdates() {
        if(isReceivingLocationUpdates) {
            LocationServices.getFusedLocationProviderClient(activity)
                .removeLocationUpdates(locationCallback)
                .addOnCompleteListener { isReceivingLocationUpdates = false }
        }
    }
}
