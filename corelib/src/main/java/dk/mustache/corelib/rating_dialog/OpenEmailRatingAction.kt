package dk.mustache.corelib.rating_dialog

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import androidx.annotation.PluralsRes
import androidx.annotation.StringRes
import dk.mustache.corelib.R

/**
 * First open a dialog asking the user if they would like to provide more
 * detailed feedback. Then open an Email client for the user to send this
 * feedback.
 *
 * Each parameter allows definition as a string or a string resource with
 * a string definition overriding a string resource. Each of these strings
 * will use a default value if not set.
 *
 * @property dialogTitle Title of confirmation dialog, as string
 * @property dialogTitleId Title of confirmation dialog, as string resource
 * @property dialogMessage Body of confirmation dialog, as string
 * @property dialogMessageId Body of confirmation dialog, as string
 *     resource
 * @property dialogConfirm Name of the confirm button of confirmation
 *     dialog, as string
 * @property dialogConfirmId Name of the confirm button of confirmation
 *     dialog, as string resource
 * @property dialogCancel Name of the cancel button of confirmation dialog,
 *     as string
 * @property dialogCancelId Name of the cancel button of confirmation
 *     dialog, as string resource
 * @property emailChooserMessage Header for the chooser that the user uses
 *     to choose email client, as string
 * @property emailChooserMessageId Header for the chooser that the user
 *     uses to choose email client, as string resource
 * @property emailRecipient Email address for the feedback to be sent to,
 *     as string
 * @property emailRecipientId Email address for the feedback to be sent to,
 *     as string resource
 * @property emailSubject Email subject, as string
 * @property emailSubjectId Email subject, as string resource
 * @property emailMessage Email message, as string
 * @property emailMessageId Email message, as string resource
 */
class OpenEmailRatingAction(
    val dialogTitle: String? = null,
    @StringRes val dialogTitleId: Int? = null,
    val dialogMessage: String? = null,
    @StringRes val dialogMessageId: Int? = null,
    val dialogConfirm: String? = null,
    @StringRes val dialogConfirmId: Int? = null,
    val dialogCancel: String? = null,
    @StringRes val dialogCancelId: Int? = null,
    val emailChooserMessage: String? = null,
    @StringRes val emailChooserMessageId: Int? = null,
    val emailRecipient: String? = null,
    @StringRes val emailRecipientId: Int? = null,
    val emailSubject: String? = null,
    @StringRes val emailSubjectId: Int? = null,
    val emailMessage: String? = null,
    @PluralsRes val emailMessageId: Int? = null
) : AppRatingConfigBuilder.NegativeRatingActions {
    override fun action(context: Context): (Int) -> Unit {
        return { rating ->
            val alertDialog = AlertDialog.Builder(context).create()
            alertDialog.setTitle(
                context.getString(
                    dialogTitle,
                    dialogTitleId,
                    R.string.rating_dialog_email_action_dialog_title
                )
            )
            alertDialog.setMessage(
                context.getString(
                    dialogMessage,
                    dialogMessageId,
                    R.string.rating_dialog_email_action_dialog_message
                )
            )
            alertDialog.setButton(
                AlertDialog.BUTTON_POSITIVE,
                context.getString(
                    dialogConfirm,
                    dialogConfirmId,
                    R.string.rating_dialog_email_action_dialog_confirm
                )
            ) { _, _ ->
                val emailIntent = Intent(Intent.ACTION_SEND)
                emailIntent.type = "message/rfc822"
                emailIntent.putExtra(
                    Intent.EXTRA_EMAIL,
                    arrayOf(
                        context.getString(
                            emailRecipient,
                            emailRecipientId,
                            R.string.rating_dialog_email_action_email_recipient
                        )
                    )
                )
                emailIntent.putExtra(
                    Intent.EXTRA_SUBJECT,
                    context.getString(
                        emailSubject,
                        emailSubjectId,
                        R.string.rating_dialog_email_action_email_subject
                    )
                )
                emailIntent.putExtra(
                    Intent.EXTRA_TEXT,
                    context.getQuantityString(
                        emailMessage,
                        emailMessageId,
                        R.plurals.rating_dialog_email_action_email_message,
                        rating,
                        rating.toString()
                    )
                )
                context.startActivity(
                    Intent.createChooser(
                        emailIntent,
                        context.getString(
                            emailChooserMessage,
                            emailChooserMessageId,
                            R.string.rating_dialog_email_action_chooser_message
                        )
                    )
                )
                alertDialog.dismiss()
            }
            alertDialog.setButton(
                AlertDialog.BUTTON_NEGATIVE,
                context.getString(
                    dialogCancel,
                    dialogCancelId,
                    R.string.rating_dialog_email_action_dialog_cancel
                )
            ) { _, _ -> alertDialog.dismiss() }
            alertDialog.show()
        }
    }

    private fun Context.getString(
        string: String?,
        @StringRes stringId: Int?,
        @StringRes defStringId: Int
    ): String {
        return if (string != null) {
            string
        } else if (stringId != null) {
            getString(stringId)
        } else {
            getString(defStringId)
        }
    }

    private fun Context.getQuantityString(
        string: String?,
        @PluralsRes stringId: Int?,
        @PluralsRes defStringId: Int,
        quantity: Int,
        vararg args: String
    ): String {
        return if (string != null) {
            string
        } else if (stringId != null) {
            resources.getQuantityString(stringId, quantity, args)
        } else {
            resources.getQuantityString(defStringId, quantity, args)
        }
    }
}