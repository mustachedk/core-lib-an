package dk.mustache.corelibexample

import android.app.Application
import android.os.Bundle
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.beacon.BeaconScanActivity
import dk.mustache.corelibexample.front.FrontFragment

class MainActivity : BeaconScanActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        MustacheCoreLib.init(application as Application)

        supportFragmentManager
            .beginTransaction()
            .replace(R.id.container, FrontFragment())
            .addToBackStack(null)
            .commit()
    }
}
