package dk.mustache.corelibexample.standard_dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import dk.mustache.corelib.fragment_dialog.DialogTypeEnum
import dk.mustache.corelib.fragment_dialog.FragmentDialogSetup
import dk.mustache.corelib.fragment_dialog.StandardDialogFragment.BaseDialogFragmentListener
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentStandardDialogDemoBinding

class StandardDialogDemoFragment: Fragment() {

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

        binding.btnOpenDialog.setOnClickListener {
            val dialog = StandardDialogExampleFragment.newInstance(
                FragmentDialogSetup(
                    "Test Dialog",
                    "This is a test of the dialog",
                    DialogTypeEnum.ALERT
                )
            )
            dialog.listener = makeDialogListener()
            dialog.show(requireActivity().supportFragmentManager, "TestDialog")
        }
    }

    private fun makeDialogListener(): BaseDialogFragmentListener<DialogTypeEnum> {
        return object : BaseDialogFragmentListener<DialogTypeEnum> {
            override fun dialogButtonClicked(index: Int, dialogType: DialogTypeEnum) {
                Toast.makeText(requireContext(), "Selected buttonindex: $index", Toast.LENGTH_LONG).show()
            }

            override fun nothingSelected() {
                Toast.makeText(requireContext(), "Nothing Selected", Toast.LENGTH_LONG).show()
            }
        }
    }
}