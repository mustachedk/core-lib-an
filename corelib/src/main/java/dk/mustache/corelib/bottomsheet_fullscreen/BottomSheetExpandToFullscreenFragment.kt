package dk.mustache.corelib.bottomsheet_fullscreen

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.util.DisplayMetrics
import android.view.*
import android.widget.FrameLayout
import android.widget.LinearLayout
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.content.ContextCompat
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dk.mustache.corelib.MustacheCoreLib
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.FragmentBottomsheetExpandFullscreenBackgroundBinding
import dk.mustache.corelib.utils.toPx

abstract class BottomSheetExpandToFullscreenFragment<T : Parcelable, Q : ViewDataBinding> : BottomSheetDialogFragment() {

    lateinit var item: T
    private var expand: Boolean = false
    private lateinit var backgroundBinding: FragmentBottomsheetExpandFullscreenBackgroundBinding
    private lateinit var binding: Q
    var wrapContentHeight = 0
    private var currentState : Int = 0

    open fun setViewBinding(binding: Q) {
        val childLayoutBinding = binding

        val backgroundLayout: LinearLayout = backgroundBinding.expandableDataLayout
        backgroundLayout.removeAllViews()

        backgroundLayout.addView(childLayoutBinding.root, 0)

        this.binding = binding
    }

    companion object {
        const val CUSTOM_ITEM = "custom_item"
        const val OPEN_EXPANDED = "open_expanded"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(DialogFragment.STYLE_NORMAL, R.style.BottomSheetProductDetailsFullscreenStyle)

        if (arguments!=null) {
            item = arguments?.getParcelable(CUSTOM_ITEM)?: throw Exception("")
            expand = arguments?.getBoolean(OPEN_EXPANDED)?: false
        }
        currentState = if (expand) {
            BottomSheetBehavior.STATE_EXPANDED
        } else {
            BottomSheetBehavior.STATE_HALF_EXPANDED
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        backgroundBinding = FragmentBottomsheetExpandFullscreenBackgroundBinding.inflate(inflater, container, false)

        return backgroundBinding.root
    }

    fun setDialogHidden(behavior: BottomSheetBehavior<FrameLayout>) {
        setNonFullscreenVisuals()
        behavior.state = BottomSheetBehavior.STATE_HIDDEN
        setNewState(BottomSheetBehavior.STATE_HIDDEN)
    }

    fun setDialogHalfExpanded(behavior: BottomSheetBehavior<FrameLayout>) {
        setNonFullscreenVisuals()
        behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
        setNewState(BottomSheetBehavior.STATE_HALF_EXPANDED)
    }

    fun setNonFullscreenVisuals() {
        if (backgroundBinding.expanableBottomsheetLayout.background != ContextCompat.getDrawable(
                MustacheCoreLib.getContextCheckInit(),
                R.drawable.bottom_sheet_menu_rounded
            )
        ) {
            backgroundBinding.expanableBottomsheetLayout.background =
                ContextCompat.getDrawable(
                    MustacheCoreLib.getContextCheckInit(),
                    R.drawable.bottom_sheet_menu_rounded
                )
            backgroundBinding.topDragDivider.visibility = View.VISIBLE
            dialog?.window?.statusBarColor =
                ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.transparent)
        }
    }

    fun setFullscreenVisuals() {
        if (backgroundBinding.expanableBottomsheetLayout.background != ContextCompat.getDrawable(
                MustacheCoreLib.getContextCheckInit(),
                R.drawable.bottom_sheet_product_details_white
            )
        ) {
            backgroundBinding.expanableBottomsheetLayout.background =
                ContextCompat.getDrawable(
                    MustacheCoreLib.getContextCheckInit(),
                    R.drawable.bottom_sheet_product_details_white
                )
            backgroundBinding.topDragDivider.visibility = View.GONE
        }
    }

    fun setDialogFullscreen(behavior: BottomSheetBehavior<FrameLayout>) {
        setFullscreenVisuals()
        behavior.state = BottomSheetBehavior.STATE_EXPANDED
        setNewState(BottomSheetBehavior.STATE_EXPANDED)
    }

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

            wrapContentHeight = this.backgroundBinding.root.height

            val ratio = wrapContentHeight.toFloat() / windowHeight.toFloat()

            val behavior = BottomSheetBehavior.from(bottomSheet)
            behavior.peekHeight = layoutParams.height + 130.toPx()
            bottomSheet.layoutParams = layoutParams
            behavior.isFitToContents = false
            behavior.halfExpandedRatio = ratio
            if (expand) {
                setDialogFullscreen(behavior)
            } else {
                setDialogHalfExpanded(behavior)
            }
            behavior.skipCollapsed = false
            behavior.isHideable = true
            dialog.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            var lockSlide = true
            Handler().postDelayed({
                lockSlide = false
            }, 2000)
            behavior.addBottomSheetCallback(object: BottomSheetBehavior.BottomSheetCallback(){
                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    when (newState) {
                        BottomSheetBehavior.STATE_EXPANDED -> {
                            setFullscreenVisuals()
                            dialog?.window?.statusBarColor =
                                ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.white)
                        }
                        BottomSheetBehavior.STATE_HIDDEN -> {
                            setNonFullscreenVisuals()
                        }
                        BottomSheetBehavior.STATE_HALF_EXPANDED -> {
                            setNonFullscreenVisuals()
                        }
                        else -> {

                        }
                    }
                    setNewState(newState)
                }

                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                    if (!lockSlide) {
                        val upperState = 0.66f
                        val lowerState = 0.33f
                        val sOffsetAbs = Math.abs(slideOffset)
                        if (behavior.state == BottomSheetBehavior.STATE_SETTLING) {
                            if (sOffsetAbs >= upperState) {
                                behavior.state = BottomSheetBehavior.STATE_HIDDEN
                            }
                            if (sOffsetAbs > lowerState && sOffsetAbs < upperState) {
                                behavior.state = BottomSheetBehavior.STATE_HALF_EXPANDED
                            }
                            if (sOffsetAbs <= lowerState && sOffsetAbs > 0.1f) {
                                behavior.state = BottomSheetBehavior.STATE_EXPANDED
                            }
                        } else if (sOffsetAbs < 0.17f) {
                            setFullscreenVisuals()
                            dialog.window?.statusBarColor =
                                ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.white)
                        } else if (sOffsetAbs < 0.33f && sOffsetAbs >= 0.17f) {
                                setFullscreenVisuals()
                                dialog?.window?.statusBarColor =
                                    ContextCompat.getColor(MustacheCoreLib.getContextCheckInit(), R.color.transparent)
                            } else {
                                setNonFullscreenVisuals()
                            }
                    }
                }
            })
        }
//        mBottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

    }

    private fun getDesiredBottomSheetDialogHeight(context: Context, topMargin: Int): Int {
        // Calculate window height for fullscreen use
        val displayMetrics = DisplayMetrics()
        (context as Activity?)!!.windowManager.defaultDisplay.getMetrics(displayMetrics)
        val displayHeight= displayMetrics.heightPixels
        return displayHeight.minus(topMargin)
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        context?.let { configureFullScreenBottomSheetDialog(
            it, dialog as BottomSheetDialog,
            0
        ) }
        return dialog
    }

    fun setNewState(newState: Int) {
        val oldState = currentState
        currentState = newState
        dialogStateChanged(oldState, newState)
    }

    abstract fun dialogStateChanged(oldState: Int, newState: Int)
}
