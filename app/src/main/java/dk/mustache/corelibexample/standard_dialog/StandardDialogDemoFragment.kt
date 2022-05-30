package dk.mustache.corelibexample.standard_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.BaseDialogFragmentListener
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.BUTTON_CANCEL
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.BUTTON_OK
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.Companion.TEXT_CLICKED
import dk.mustache.corelibexample.databinding.FragmentStandardDialogDemoBinding

class StandardDialogDemoFragment : Fragment(), BaseDialogFragmentListener<DialogTypeEnum> {

    lateinit var binding: FragmentStandardDialogDemoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStandardDialogDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnOpenAlertDialog.setOnClickListener {
            createAlertDialog().show(requireActivity().supportFragmentManager, "AlertDialog")
        }

        binding.btnOpenCustomDialog.setOnClickListener {
            createCustomDialog().show(requireActivity().supportFragmentManager, "CustomDialog")
        }
    }

    private fun createAlertDialog(): StandardDialogFragment<DialogTypeEnum> {
        var dialog = StandardDialogFragment<DialogTypeEnum>()
        dialog = StandardDialogFragment.setupDialog(
            FragmentDialogSetup(
                "Alert Dialog",
                "This is a test of the alert dialog",
                DialogTypeEnum.ALERT
            ),
            dialog
        )
        dialog.listener = this
        return dialog
    }

    private fun createCustomDialog(): CustomStandardDialog {
        val dialog = CustomStandardDialog.newInstance(
            "Custom Dialog",
            "This is a test of the custom dialog",
            this
        )
        return dialog
    }

    override fun dialogButtonClicked(index: Int, dialogType: DialogTypeEnum) {
        val text = when (index) {
            TEXT_CLICKED -> "Text"
            BUTTON_OK -> "OK"
            BUTTON_CANCEL -> "Cancel"
            else -> {
                "Unknown Index"
            }
        }
        Toast.makeText(requireContext(), "User clicked: $text", Toast.LENGTH_SHORT).show()
    }

    override fun nothingSelected() {
        Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_SHORT).show()
    }
}