package dk.mustache.corelibexample.standard_dialog_example

import android.os.Bundle
import android.view.View
import dk.mustache.corelib.databinding.FragmentDialogCustomBinding
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment

class StandardDialogExampleFragment: StandardDialogFragment() {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val dialogBinding = binding
        when(dialogBinding) {
            is FragmentDialogCustomBinding -> {
                dialogBinding.positiveButton.setOnClickListener {
                    requireActivity().finish()
                }
                dialogBinding.negativeButton.setOnClickListener {
                    dismiss()
                }
            }
            else -> {

            }
        }
    }

    companion object {
        private const val DIALOG_SETUP = "dialog_setup"

        fun newInstance(dialogSetup: FragmentDialogSetup): StandardDialogExampleFragment {
            val fragment = StandardDialogExampleFragment()
            val args = Bundle()
            args.putParcelable(DIALOG_SETUP, dialogSetup)

            fragment.arguments = args

            return fragment
        }
    }
}