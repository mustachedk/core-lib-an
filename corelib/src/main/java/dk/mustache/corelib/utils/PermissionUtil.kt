package dk.mustache.corelib.utils

import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import dk.mustache.corelib.R

/**
 * Various suggested Request Codes
 */
const val RC_READ_CALENDAR = 1001
const val RC_WRITE_CALENDAR = 1002
const val RC_CAMERA = 1003
const val RC_READ_CONTACTS = 1004
const val RC_WRITE_CONTACTS = 1005
const val RC_GET_ACCOUNTS = 1006
const val RC_ACCESS_FINE_LOCATION = 1007
const val RC_ACCESS_COARSE_LOCATION = 1008
const val RC_RECORD_AUDIO = 1009
const val RC_READ_PHONE_STATE = 1010
const val RC_READ_PHONE_NUMBERS = 1011
const val RC_CALL_PHONE = 1012
const val RC_ANSWER_PHONE_CALLS = 1013
const val RC_READ_CALL_LOG = 1014
const val RC_WRITE_CALL_LOG = 1015
const val RC_ADD_VOICEMAIL = 1016
const val RC_USE_SIP = 1017
const val RC_PROCESS_OUTGOING_CALLS = 1018
const val RC_BODY_SENSORS = 1019
const val RC_SEND_SMS = 1020
const val RC_RECEIVE_SMS = 1021
const val RC_READ_SMS = 1022
const val RC_RECEIVE_WAP_PUSH = 1023
const val RC_RECEIVE_MMS = 1024
const val RC_READ_EXTERNAL_STORAGE = 1025
const val RC_WRITE_EXTERNAL_STORAGE = 1026

/**
 * Determine whether user have granted a particular permission.
 *
 * @param context The context on which to check the permission
 * @param permission The name of the permission being checked.
 *
 * @return [PackageManager.PERMISSION_GRANTED] if you have the permission, or [PackageManager.PERMISSION_DENIED] if not.
 */
fun hasPermission(context: Context, permission: String): Boolean { return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED }

/**
 * Request a particular permission.
 *
 * @param activity The target activity
 * @param permission The permission to be requested.
 * @param requestCode Application specific request code to match with a result - reported to onRequestPermissionsResult(int, String[], int[]). Should be >= 0.
 */
fun requestPermission(activity: Activity, permission: String, requestCode: Int): Boolean {
    return if(!hasPermission(activity, permission)) {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
        false
    } else {
        true
    }
}

/**
 * Request a multiple permissions.
 *
 * @param activity The target activity
 * @param permissions The permissions to be requested.
 * @param requestCode Application specific request code to match with a result - reported to onRequestPermissionsResult(int, String[], int[]). Should be >= 0.
 */
fun requestPermissions(activity: Activity, permissions: Array<String>, requestCode: Int) {
    ActivityCompat.requestPermissions(activity, permissions, requestCode)
}

/**
 * Request a particular permission, but show the permission rationale first if not shown
 *
 * @param activity The target activity
 * @param permission The permission to be requested.
 * @param requestCode Application specific request code to match with a result - reported to onRequestPermissionsResult(int, String[], int[]). Should be >= 0.
 * @param rationaleTitle The title to display in the rationale AlertDialog
 * @param rationaleMessage The message to display in the rationale AlertDialog
 * @param rationalePositiveButton The button text to display in the rationale AlertDialog
 */
fun requestPermissionWithRationale(activity: Activity, permission: String, requestCode: Int, rationaleTitle: String?, rationaleMessage: String?, rationalePositiveButton: String?) {
    if(ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)) {
        AlertDialog.Builder(activity)
            .setTitle(rationaleTitle ?: activity.getString(R.string.rationale_title))
            .setMessage(rationaleMessage ?: activity.getString(R.string.rationale_message))
            .setPositiveButton(rationalePositiveButton ?: activity.getString(android.R.string.ok)) { _, _ ->
                requestPermission(activity, permission, requestCode)
            }
            .create()
            .show()
    } else {
        ActivityCompat.requestPermissions(activity, arrayOf(permission), requestCode)
    }
}
