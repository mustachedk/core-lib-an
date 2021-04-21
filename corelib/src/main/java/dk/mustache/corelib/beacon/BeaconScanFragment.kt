package dk.mustache.corelib.beacon

import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.RemoteException
import androidx.fragment.app.Fragment
import dk.mustache.corelib.utils.keepScreenOn
import org.altbeacon.beacon.Beacon
import org.altbeacon.beacon.BeaconConsumer
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.Region

abstract class BeaconScanFragment : Fragment(), BeaconConsumer {

    private var beaconManager: BeaconManager? = null
    private var isScanning = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    private fun startScan() {
        if (beaconManager?.isBound(this) != true) {
            beaconManager?.bind(this)
        }

        activity?.keepScreenOn(true)

        isScanning = true
    }

    private fun stopScan() {
        unbindBeaconManager()
        activity?.keepScreenOn(false)
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
                beaconsFound(beacons.toList())
            }
        }

        try {
            beaconManager?.startRangingBeaconsInRegion(Region("dk.mustache.corelib", null, null, null))
        } catch (e: RemoteException) {
            e.printStackTrace()
        }

//        stopScan()
    }

    override fun getApplicationContext(): Context {
        return requireActivity().applicationContext
    }

    override fun unbindService(p0: ServiceConnection?) {
        if (p0!=null) {
            requireActivity().unbindService(p0)
        }
    }

    override fun bindService(p0: Intent?, p1: ServiceConnection?, p2: Int): Boolean {
        return if (p1!=null) {
            requireActivity().bindService(p0, p1, p2)
        } else {
            false
        }
    }

    override fun onPause() {
        unbindBeaconManager()
        activity?.keepScreenOn(false)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    abstract fun beaconsFound(beacons: List<Beacon>)
}