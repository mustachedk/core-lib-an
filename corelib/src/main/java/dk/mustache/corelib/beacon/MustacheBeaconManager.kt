package dk.mustache.corelib.beacon

import dk.mustache.corelib.MustacheCoreLib
import org.altbeacon.beacon.BeaconManager
import org.altbeacon.beacon.BeaconParser
import org.altbeacon.beacon.BeaconParser.*

object MustacheBeaconManager {
    const val RUUVI_LAYOUT = "m:0-2=0499,i:4-19,i:20-21,i:22-23,p:24-24" // TBD
    const val IBEACON_LAYOUT = "m:0-3=4c000215,i:4-19,i:20-21,i:22-23,p:24-24"
    const val ALTBEACON_LAYOUT = BeaconParser.ALTBEACON_LAYOUT

    fun getBeaconManager(): BeaconManager {
        val instance = BeaconManager.getInstanceForApplication(MustacheCoreLib.getContextCheckInit())

        // Sets the delay between each scans according to the settings
        instance.foregroundBetweenScanPeriod = 1000L

        // Add all the beacon types we want to discover
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(IBEACON_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_UID_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_URL_LAYOUT))
        instance.beaconParsers.add(BeaconParser().setBeaconLayout(EDDYSTONE_TLM_LAYOUT))
        return instance
    }
}