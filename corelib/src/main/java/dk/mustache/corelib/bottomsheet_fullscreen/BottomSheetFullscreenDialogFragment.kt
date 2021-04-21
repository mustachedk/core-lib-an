package dk.mustache.corelib.bottomsheet_fullscreen

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.widget.FrameLayout
import androidx.core.view.ViewCompat.getDisplay
import com.google.android.material.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

abstract class BottomSheetFullScreenDialogFragment : BottomSheetDialogFragment() {
    fun configureFullScreenBottomSheetDialog(context: Context, bottomSheetDialog: BottomSheetDialog, topMargin: Int) {
        bottomSheetDialog.setOnShowListener { dia ->
            val dialog = dia as BottomSheetDialog
            val bottomSheet =
                dialog.findViewById<FrameLayout>(R.id.design_bottom_sheet)
            val layoutParams = bottomSheet!!.layoutParams
            val windowHeight: Int = getDesiredBottomSheetDialogHeight(context, topMargin)
            if (layoutParams != null) {
                layoutParams.height = windowHeight
            }
            BottomSheetBehavior.from(bottomSheet).peekHeight = layoutParams.height
            bottomSheet.layoutParams = layoutParams
            BottomSheetBehavior.from(bottomSheet).state = BottomSheetBehavior.STATE_EXPANDED
            BottomSheetBehavior.from(bottomSheet).skipCollapsed = false
            BottomSheetBehavior.from(bottomSheet).isHideable = true
        }
    }

    private fun getDesiredBottomSheetDialogHeight(context: Context, topMargin: Int): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()

        @Suppress("DEPRECATION")
        val display = requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight= displayMetrics.heightPixels
        return displayHeight.minus(topMargin)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        context?.let { configureFullScreenBottomSheetDialog(
            it, dialog as BottomSheetDialog,
            getTopMargin()
        ) }
        return dialog
    }

    abstract fun getTopMargin(): Int

}