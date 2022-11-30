package dk.mustache.corelib.views

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.ContextThemeWrapper
import androidx.annotation.StringRes
import androidx.annotation.StyleRes
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import dk.mustache.corelib.R

class ErrorDialog(
    @StringRes private val titleId: Int? = null,
    private val title: String? = null,
    @StringRes private val messageId: Int? = null,
    private val message: String? = null
) : DialogFragment() {
    companion object {
        @StyleRes private var themeResId: Int? = null
        @StringRes private var defaultTitle: Int? = null
        @StringRes private var defaultBody: Int? = null
        @StringRes private var defaultButtonText: Int? = null

        /** Resets text and styling to the defaults for all future ErrorDialog calls. */
        fun resetStyling() {
            defaultTitle = null
            defaultBody = null
            defaultButtonText = null
            themeResId = null
        }

        /**
         * Set theme for future ErrorDialogs
         *
         * @param themeResId Id of the style to apply to the ErrorDialogs
         */
        fun setTheme(@StyleRes themeResId: Int) {
            this.themeResId = themeResId
        }

        /**
         * Set default strings. These are used if nothing else is defined when an ErrorDialog is called.
         *
         * @param titleStringId Default value for the dialog title
         * @param bodyStringId Default value for the dialog body
         * @param buttonStringId Value for the dialog button text.
         */
        fun setDefaultStrings(@StringRes titleStringId: Int, @StringRes bodyStringId: Int, @StringRes buttonStringId: Int) {
            defaultTitle = titleStringId
            defaultBody = bodyStringId
            defaultButtonText = buttonStringId
        }

        /**
         * Show ErrorDialog with custom title and/or body
         *
         * @param fragment The fragment to show the ErrorDialog on.
         */
        fun show(fragment: Fragment, @StringRes titleId: Int? = null, @StringRes messageId: Int? = null) {
            ErrorDialog(titleId = titleId, messageId = messageId).show(fragment)
        }

        /**
         * Show ErrorDialog with custom title and/or body
         *
         * @param fragment The fragment to show the ErrorDialog on.
         */
        fun show(fragment: Fragment, title: String? = null, message: String? = null ) {
            ErrorDialog(title = title, message = message).show(fragment)
        }

        /**
         * Show ErrorDialog
         *
         * @param fragment The fragment to show the ErrorDialog on.
         * @param message Text to show in the body of the ErrorDialog. Optional.
         */
        fun show(fragment: Fragment, message: String? = null) {
            ErrorDialog(message = message).show(fragment)
        }

        /**
         * Show ErrorDialog with default title and body text extracted from the provided throwable's
         * localizedMessage.
         *
         * @param fragment The fragment to show the ErrorDialog on.
         */
        fun show( fragment: Fragment, e: Throwable) {
            ErrorDialog(message = e.localizedMessage).show(fragment)
        }
    }

    /**
     * Use companion function ErrorDialog.show() instead of this function
     *
     * @param fragment
     */
    fun show(fragment: Fragment) {
        this.show(fragment.parentFragmentManager, "Error Dialog")
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val theme = themeResId
        val builder = if(theme != null) {
            AlertDialog.Builder(ContextThemeWrapper(requireContext(), theme))
        }
        else {
            AlertDialog.Builder(requireContext())
        }

        val okButtonText = if(defaultButtonText != null) {
            defaultButtonText!!
        } else {
            R.string.error_dialog_default_button
        }

        return builder
            .setTitle(getStringOrDefault(titleId, title, defaultTitle, R.string.error_dialog_default_title))
            .setMessage(getStringOrDefault(messageId, message, defaultBody, R.string.error_dialog_default_body))
            .setPositiveButton(okButtonText) { dialog, id ->
                dialog.dismiss()
            }.create()
    }

    private fun getStringOrDefault(
        @StringRes resId: Int?,
        string: String?,
        @StringRes localDefault: Int?,
        @StringRes corelibDefault: Int
    ): String {
        return if (resId != null) {
            requireContext().getString(resId)
        } else if (string != null) {
            string
        } else if(localDefault != null) {
            requireContext().getString(localDefault)
        } else {
            requireContext().getString(corelibDefault)
        }
    }
}
