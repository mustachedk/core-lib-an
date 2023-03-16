package dk.mustache.corelib.rating_dialog

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import dk.mustache.corelib.databinding.DialogCustomRatingBinding
import dk.mustache.corelib.rating_dialog.AppRatingConfigBuilder.RatingButtonType.*

internal class RatingDialogHelper(
    private val context: Context
) {
    private val config = AppRatingManager.appRatingConfig
    private var thisDialog: AlertDialog? = null

    fun showRatingDialog(showNeverButton: Boolean = false) {
        AppRatingManager.markAsShownThisSession()

        val optionsArray = createDialogOptions(showNeverButton)
        val optionSelectedListener = createOnClickListener()
        val customView = createCustomView(optionSelectedListener, optionsArray)

        val builder = AlertDialog.Builder(context)
        builder.setTitle(config.mainDialogTitle)
        builder.setView(customView)
        thisDialog = builder.create()
        thisDialog?.show()
    }

    private fun createDialogOptions(includeNeverButton: Boolean = false): List<String> {
        val optionsArray = mutableListOf(
            "★★★★★",
            "★★★★✩",
            "★★★✩✩",
            "★★✩✩✩",
            "★✩✩✩✩",
            config.mainDialogDelay
        )
        if (includeNeverButton) {
            optionsArray.add(config.mainDialogNever)
        }
        return optionsArray
    }

    private fun createOnClickListener(): AdapterView.OnItemClickListener {
        return AdapterView.OnItemClickListener { _, _, optionIndex, _ ->
            val rating = 5 - optionIndex
            val maxIndexForPositive = 5 - config.minStarsForPositive
            when (optionIndex) {
                // 4-5 stars
                in (0..maxIndexForPositive) -> {
                    config.positiveRatingFunc?.invoke(rating)
                    if (config.disableOnPositiveAction) {
                        AppRatingManager.neverShowRatingDialogAgain()
                    }
                    AppRatingManager.appRatingConfig.onDialogueCompleted?.invoke(POSITIVE)
                }
                // 1-3 stars
                in (maxIndexForPositive + 1..4) -> {
                    config.negativeRatingFunc?.invoke(rating)
                    if (config.disableOnNegativeAction) {
                        AppRatingManager.neverShowRatingDialogAgain()
                    }
                    AppRatingManager.appRatingConfig.onDialogueCompleted?.invoke(NEGATIVE)
                }
                // delay
                5 -> {
                    AppRatingManager.markAsShownThisSession()
                    AppRatingManager.appRatingConfig.onDialogueCompleted?.invoke(DELAY)
                }
                // never
                6 -> {
                    AppRatingManager.neverShowRatingDialogAgain()
                    AppRatingManager.appRatingConfig.onDialogueCompleted?.invoke(NEVER)
                }
            }
            thisDialog?.dismiss()
        }
    }

    private fun createCustomView(
        onClick: AdapterView.OnItemClickListener,
        items: List<String>
    ): View {
        val binding = DialogCustomRatingBinding.inflate(LayoutInflater.from(context), null, false)

        val message = config.mainDialogMessage
        if (message.isNotEmpty()) {
            binding.txtMessage.text = message
        } else {
            binding.txtMessage.visibility = View.GONE
        }

        val adapter = ArrayAdapter(context, android.R.layout.simple_list_item_1, items)
        binding.lstOptions.adapter = adapter
        binding.lstOptions.onItemClickListener = onClick

        return binding.root
    }
}