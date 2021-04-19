package dk.mustache.corelib.bluetooth

import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

object BluetoothManager {
    const val REQUEST_ENABLE_BT = 0
    private val bluetoothAdapter = lazy { BluetoothAdapter.getDefaultAdapter() }

    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.value!=null && bluetoothAdapter.value.isEnabled
    }

    fun enableBluetooth(activity: Activity) {
        if (!bluetoothAdapter.value.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}