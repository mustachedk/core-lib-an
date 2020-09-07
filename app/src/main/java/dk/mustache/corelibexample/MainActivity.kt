package dk.mustache.corelibexample

import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import dk.mustache.corelib.utils.LocationUtil
import dk.mustache.corelib.utils.RC_ACCESS_FINE_LOCATION
import dk.mustache.corelib.utils.hasPermission
import dk.mustache.corelib.utils.requestPermissionWithRationale
import dk.mustache.corelibexample.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity(), LocationUtil.LocationChangedCallback {

    private var locationUtil: LocationUtil? = null
    private var currentLocation: Location? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        locationUtil = LocationUtil(this)
        locationUtil?.registerLocationListener(this)
        requestPermissionWithRationale(
            this,
            ACCESS_FINE_LOCATION,
            RC_ACCESS_FINE_LOCATION,
            "Test Title",
            "Test Message",
            "Test Button"
        )

        var binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
        binding.del = this
    }

    override fun onResume() {
        super.onResume()
        locationUtil?.startLocationUpdates()
    }

    override fun onPause() {
        super.onPause()
        locationUtil?.stopLocationUpdates()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        when (requestCode) {
            RC_ACCESS_FINE_LOCATION -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(hasPermission(this, ACCESS_FINE_LOCATION)) {
                        /** User granted location permission */
                    } else {
                        /** We don't have the location permission */
                    }
                } else {
                    /** User denied location permission */
                }
            }
        }
    }

    override fun locationChanged(location: Location) {
        currentLocation = location
    }
}
