package dk.mustache.corelibexample.errordialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import dk.mustache.corelib.views.ErrorDialog
import dk.mustache.corelibexample.R
import dk.mustache.corelibexample.databinding.FragmentErrorDialogDemoBinding

class ErrorDialogDemoFragment : Fragment() {

    lateinit var binding: FragmentErrorDialogDemoBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentErrorDialogDemoBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.btnDefault.setOnClickListener {
            ErrorDialog.show(this)
        }
        //
        // Dialog styling functions
        // Note that these are one-and-done. Set style/default text in App/MainActivity, and all
        // the ErrorDialogs opened in the app will have that styling.
        //
        binding.btnDefaultStyledDialog.setOnClickListener {
            ErrorDialog.resetStyling() // By default ErrorDialog uses the default style, so this call is not necessary unless ErrorDialog.setTheme() has been called previously
            ErrorDialog.show(
                this,
                "Further dialogs will be styled according to the apps default dialog styling (and use the corelib default text)."
            )
        }
        binding.btnCustomStyledDialog.setOnClickListener {
            ErrorDialog.setTheme(R.style.ErrorDialogDemoTheme)
            ErrorDialog.show(
                this,
                "Further dialogs will be styled according to the custom style just set."
            )
        }
        binding.btnSetLocalDefaultText.setOnClickListener {
            ErrorDialog.setDefaultStrings(
                R.string.error_dialog_title_local_default,
                R.string.error_dialog_body_local_default,
                R.string.error_dialog_button_local_default
            )
            ErrorDialog.show(
                this,
                "Default text is now set."
            )
        }
        //
        // ErrorDialog.show functions
        //
        binding.btnByException.setOnClickListener {
            ErrorDialog.show(
                this,
                IllegalAccessException("You are not allowed to press this button.")
            )
        }
        binding.btnByStringResIds.setOnClickListener {
            ErrorDialog.show(this, R.string.error_dialog_title, R.string.error_dialog_body)
        }
        binding.btnByStrings.setOnClickListener {
            ErrorDialog.show(this, "Showing an error with custom message, but default title. " +
                    "It is also possible to have a custom title.")
        }
    }
}