package dk.mustache.corelibexample.standard_dialog

import android.os.Bundle
import android.view.View
import android.widget.Toast
import dk.mustache.corelib.databinding.FragmentDialogCustomBinding
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.DialogCustomStandardBinding

/**
 * Uncomment the BaseDialogFragmentListener interface and override
 * methods, and this implementation can be its own listener
 */
class CustomStandardDialog(dialogListener: BaseDialogFragmentListener<DialogTypeEnum>? = null) :
    StandardDialogFragment<DialogTypeEnum>()
//    BaseDialogFragmentListener<DialogTypeEnum>
{
    init {
        if(dialogListener != null) {
            listener = dialogListener
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogBinding = binding
        when (dialogBinding) {
            is DialogCustomStandardBinding -> {
                dialogBinding.positiveButton.setOnClickListener {
                    listener?.dialogButtonClicked(BUTTON_OK, DialogTypeEnum.CUSTOM)
                    dismiss()
                }
                dialogBinding.negativeButton.setOnClickListener {
                    listener?.dialogButtonClicked(BUTTON_CANCEL, DialogTypeEnum.CUSTOM)
                    dismiss()
                }
            }
            else -> {

            }
        }
    }

    companion object {
        fun newInstance(
            header: String,
            text: String,
            dialogListener: BaseDialogFragmentListener<DialogTypeEnum>? = null
        ): CustomStandardDialog {
            var fragment = CustomStandardDialog(dialogListener)
            val dialogSetup = FragmentDialogSetup(
                header,
                text,
                DialogTypeEnum.CUSTOM,
                alternativeLayout = R.layout.dialog_custom_standard
            )
            fragment = setupDialog(
                dialogSetup,
                fragment
            ) as CustomStandardDialog
            return fragment
        }
    }

//    override fun dialogButtonClicked(index: Int, dialogType: DialogTypeEnum) {
//        val text = when (index) {
//            TEXT_CLICKED -> dialogSetup.text
//            BUTTON_OK -> "OK"
//            BUTTON_CANCEL -> "Cancel"
//            else -> {
//                "Unknown Index"
//            }
//        }
//        Toast.makeText(requireContext(), "User clicked: $text; from dialog", Toast.LENGTH_LONG).show()
//    }
//
//    override fun nothingSelected() {
//        Toast.makeText(requireContext(), "Nothing Selected; from dialog", Toast.LENGTH_LONG).show()
//    }
}