package dk.mustache.corelib

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.Criteria
import android.location.Location
import android.location.LocationListener
import android.location.LocationManager
import android.os.Bundle
import androidx.annotation.RequiresPermission
import androidx.core.app.ActivityCompat

class GPSSingleShotCurrentLocation {

    interface SingleLocationListener {
        fun onCurrentLocationAvailable(location: Location?)
        fun onMissingPermission()
        fun onGPSNotEnabled()
    }

    @RequiresPermission("android.permission.ACCESS_FINE_LOCATION")
    fun requestSingleUpdate(context: Context, requireGps: Boolean = false, listener: SingleLocationListener) {
        val locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager
        val isNetworkEnabled = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)
        val isGPSEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
        val criteria = Criteria()
        if (isGPSEnabled) {
            criteria.accuracy = Criteria.ACCURACY_FINE
        } else {
            criteria.accuracy = Criteria.ACCURACY_COARSE
        }
        if ((!requireGps && isNetworkEnabled) || isGPSEnabled) {
            locationManager.requestSingleUpdate(criteria, object : LocationListener {
                override fun onLocationChanged(location: Location) {
                    listener.onCurrentLocationAvailable(location)
                    locationManager.removeUpdates(this)
                }

                override fun onStatusChanged(provider: String, status: Int, extras: Bundle) {}
                override fun onProviderEnabled(provider: String) {}
                override fun onProviderDisabled(provider: String) {}
            }, null)
        } else {
            listener.onGPSNotEnabled()
        }
    }

    private fun requestCurrentLocation(context: Context, listener: SingleLocationListener) {
        if (ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED &&
            ActivityCompat.checkSelfPermission(
                context,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            listener.onMissingPermission()
        } else {
            requestSingleUpdate(context,listener = listener)
        }
    }
}