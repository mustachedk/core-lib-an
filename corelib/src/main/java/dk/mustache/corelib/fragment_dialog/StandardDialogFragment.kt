package dk.mustache.corelib.fragment_dialog

import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.Window
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.DialogFragment
import dk.mustache.corelib.BR
import dk.mustache.corelib.R
import dk.mustache.corelib.databinding.FragmentAlertDialogBinding
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup

open class StandardDialogFragment <T: Enum<T>> : DialogFragment() {

    var mListener: BaseDialogFragmentListener<T>? = null
    lateinit var dialogSetup: FragmentDialogSetup<T>
    lateinit var binding: ViewDataBinding

    interface BaseDialogFragmentListener <T: Enum<T>> {
        fun dialogButtonClicked(
            index: Int,
            dialogType: T
        )
        fun nothingSelected()
    }

    companion object {
        private const val DIALOG_SETUP = "dialog_setup"
        const val BUTTON_OK = 1
        const val BUTTON_CANCEL = 2
        const val TEXT_CLICKED = 3

        fun <T: Enum<T>> setupDialog(
            dialogSetup: FragmentDialogSetup<T>,
            fragment: StandardDialogFragment<T>
        ): StandardDialogFragment<T> {
            val args = Bundle()
            args.putParcelable(DIALOG_SETUP, dialogSetup)

            fragment.arguments = args

            return fragment
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (arguments!=null) {
            val setup = arguments?.getParcelable<FragmentDialogSetup<T>>(DIALOG_SETUP)
            if (setup!=null) {
                dialogSetup = setup
            }
        }

        if (dialogSetup.alternativeStyle!=0 && dialogSetup.setAlternativeStyleIfProvided) {
            setStyle(STYLE_NO_TITLE, dialogSetup.alternativeStyle)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = when (dialogSetup.dialogType) {
            DialogTypeEnum.ALERT  -> {
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, R.layout.fragment_alert_dialog, container, false)
            }
            else -> {
                DataBindingUtil.inflate<ViewDataBinding>(layoutInflater, dialogSetup.alternativeLayout, container, false)
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        val dialogBinding = binding
        when(dialogBinding) {
            is FragmentAlertDialogBinding -> {
                dialogBinding.backgroundDim.visibility = View.GONE
                dialogBinding.dialogHeader.text = dialogSetup.header
                dialogBinding.dialogText1.text = Html.fromHtml(dialogSetup.text)

                dialogBinding.dialogText1.setOnClickListener {
                    mListener?.dialogButtonClicked(TEXT_CLICKED, dialogSetup.dialogType)
                }

                dialogBinding.buttonOk.setOnClickListener {
                    dismiss()
                    mListener?.dialogButtonClicked(BUTTON_OK, dialogSetup.dialogType)
                }
                dialogBinding.buttonNo.setOnClickListener {
                    dismiss()
                    mListener?.dialogButtonClicked(BUTTON_CANCEL, dialogSetup.dialogType)
                }
            }
            else -> {
                try {
                    dialogBinding.setVariable(BR.dialogSetup, dialogSetup)
                    dialogBinding.executePendingBindings()
                } catch (e: Exception) {

                }
            }
        }


    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        dialog?.window?.attributes?.windowAnimations = if (dialogSetup.alternativeStyle!=0) {
            dialogSetup.alternativeStyle
        } else {
            R.style.BottomSheetDialogStyle
        }

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M && dialogSetup.darkStatusBarButtons) {
            dialog?.window?.decorView?.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }

    override fun onStart() {
        super.onStart()
        if (dialog != null) {
            val width = ViewGroup.LayoutParams.MATCH_PARENT
            val height = ViewGroup.LayoutParams.MATCH_PARENT
            dialog?.window?.setLayout(width, height)
        }
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        val cardDetailsListener = parentFragment
        if (cardDetailsListener is BaseDialogFragmentListener<*>)
            mListener = cardDetailsListener as BaseDialogFragmentListener<T>
        else if (activity is BaseDialogFragmentListener<*>){
            mListener = activity as BaseDialogFragmentListener<T>
        } else
            throw RuntimeException(parentFragment?.tag + " must implement BaseDialogFragmentListener")
    }

    override fun onDetach() {
        super.onDetach()
        mListener?.nothingSelected()
        mListener = null
    }
}
