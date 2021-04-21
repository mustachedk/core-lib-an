package dk.mustache.corelib.beacon

import android.Manifest
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.PorterDuff
import android.net.Uri
import android.os.Bundle
import android.os.RemoteException
import androidx.appcompat.app.AppCompatActivity
import dk.mustache.corelib.utils.keepScreenOn
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region

open class BeaconScanActivity : AppCompatActivity(), BeaconConsumer {

    private var beaconManager: BeaconManager? = null
    private var isScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun startScan() {
        if (beaconManager?.isBound(this) != true) {
            beaconManager?.bind(this)
        }

        keepScreenOn(true)

        isScanning = true
    }

    private fun stopScan() {
        unbindBeaconManager()
        keepScreenOn(false)
        isScanning = false
    }

    fun isScanning() = isScanning

    private fun unbindBeaconManager() {
        if (beaconManager?.isBound(this) == true) {
            beaconManager?.unbind(this)
        }
    }

    override fun onResume() {
        super.onResume()
        beaconManager = MustacheBeaconManager.getBeaconManager()

        startScan()
    }

    override fun onBeaconServiceConnect() {
        beaconManager?.addRangeNotifier { beacons, _ ->
            if (isScanning) {
            }
        }

        try {
            beaconManager?.startRangingBeaconsInRegion(Region("dk.mustache.corelib", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

//        stopScan()
    }

    override fun onPause() {
        unbindBeaconManager()
        keepScreenOn(false)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }
}