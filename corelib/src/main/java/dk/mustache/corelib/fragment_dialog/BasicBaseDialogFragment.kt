package dk.mustache.corelib.fragment_dialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import androidx.fragment.app.DialogFragment
import dk.mustache.corelib.R
import java.io.Serializable

open class BasicBaseDialogFragment : DialogFragment(){

    val TRANSITION_DIRECTION = "transition_direction"
    lateinit var transitionDirection: DialogTransitionDirectionEnum

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setStyle(STYLE_NORMAL, R.style.BasicBaseDialogStyle)

        transitionDirection = (arguments?.getSerializable(TRANSITION_DIRECTION) as DialogTransitionDirectionEnum?)
                ?: DialogTransitionDirectionEnum.ENTER_BOTTOM

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }

        isCancelable = false
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.window?.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE)
        return dialog
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

    }

    override fun onActivityCreated(arg0: Bundle?) {
        super.onActivityCreated(arg0)
        when (transitionDirection) {
            DialogTransitionDirectionEnum.ENTER_BOTTOM -> {
                dialog?.window?.attributes?.windowAnimations = R.style.BasicBaseBottomDialogStyle
            }
            DialogTransitionDirectionEnum.ENTER_RIGHT -> {
                dialog?.window?.attributes?.windowAnimations = R.style.BasicBaseRightDialogStyle
            }
            else -> {
                dialog?.window?.attributes?.windowAnimations = R.style.BasicBaseDialogStyle
            }
        }
    }
}