package dk.mustache.corelib.utils

import android.app.Activity
import android.content.Context
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.google.android.material.snackbar.Snackbar
import dk.mustache.corelib.R

class DialogUtil {

    companion object {

        /**
         * Displays an AlertDialog with a callback upon user interaction.
         * The caller must implement AlertDialogCallBack.
         * If no string is passed to negativeButton it will not be displayed in the dialog.
         * Note: 'dialogId' is used to identify the source of the callbacks.
         */
        fun showAlertDialogWithCallBack(context: Context, title: String, message: String, positiveButton: String, negativeButton: String = "", dialogId: Int, isCancelable: Boolean = false) {
            val callBack: AlertDialogCallBack?

            // Ensure that the caller implements AlertDialogCallBack.
            if (context is AlertDialogCallBack) {
                callBack = context
            } else {
                throw ClassCastException(context.getString(R.string.dialog_interface_missing_error))
            }

            val dialog = AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setCancelable(isCancelable)
                .setPositiveButton(positiveButton) { _, _ ->
                    callBack.onClickConfirm(dialogId)
                }
            if (!negativeButton.isBlank()) {
                dialog.setNegativeButton(negativeButton) { _, _ ->
                    callBack.onClickCancel(dialogId)
                }
            }
            dialog.create().show()
        }

        /**
         * Displays a standard AlertDialog without callbacks.
         */
        fun showStandardDialog(context: Context, title: String, message: String, confirmButton: String, isCancelable: Boolean = false) {
            AlertDialog.Builder(context)
                .setTitle(title)
                .setMessage(message)
                .setPositiveButton(confirmButton) { _, _ -> }
                .setCancelable(isCancelable)
                .create()
                .show()
        }

        /**
         * Displays a Snackbar with a callback.
         * The caller must implement SnackbarCallBack.
         * Note: 'snackbarId' is used to identify the source of the callback.
         */
        fun showSnackBarWithCallBack(activity: Activity, message: String, confirmButton: String, snackbarId: Int, duration: Int = Snackbar.LENGTH_INDEFINITE) {
            val callBack: SnackbarCallBack

            // Ensure that the caller implements SnackbarCallBack.
            if (activity is SnackbarCallBack) {
                callBack = activity
            } else {
                throw ClassCastException(activity.getString(R.string.snackbar_interface_missing_error))
            }

            val rootView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            Snackbar.make(rootView, message, duration)
                .setAction(confirmButton) {
                    callBack.onClickSnackBarConfirm(snackbarId)
                }
                .show()
        }

        /**
         * Displays a Snackbar without a callback.
         */
        fun showSnackBar(activity: Activity, message: String, duration: Int = Snackbar.LENGTH_SHORT) {
            val rootView = activity.findViewById<ViewGroup>(android.R.id.content).getChildAt(0)
            Snackbar.make(rootView, message, duration).show()
        }
    }

    interface AlertDialogCallBack {
        fun onClickConfirm(dialogId: Int)
        fun onClickCancel(dialogId: Int)
    }

    interface SnackbarCallBack {
        fun onClickSnackBarConfirm(snackbarId: Int)
    }
}






