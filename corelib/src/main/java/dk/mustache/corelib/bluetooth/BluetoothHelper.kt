package dk.mustache.corelib.bluetooth

import android.Manifest
import android.app.Activity
import android.bluetooth.BluetoothAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import androidx.annotation.RequiresPermission
import io.reactivex.rxjava3.core.Flowable
import io.reactivex.rxjava3.processors.BehaviorProcessor

object BluetoothHelper {
    const val REQUEST_ENABLE_BT = 0
    private val bluetoothAdapter = lazy { BluetoothAdapter.getDefaultAdapter() }

    @RequiresPermission (Manifest.permission.BLUETOOTH)
    fun isBluetoothEnabled(): Boolean {
        return bluetoothAdapter.value!=null && bluetoothAdapter.value.isEnabled
    }

    @RequiresPermission (Manifest.permission.BLUETOOTH_ADMIN)
    fun enableBluetooth(activity: Activity) {
        if (!bluetoothAdapter.value.isEnabled) {
            val enableBtIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            activity.startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT)
        }
    }
}