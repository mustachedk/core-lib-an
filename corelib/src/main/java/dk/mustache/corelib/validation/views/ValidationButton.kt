package dk.mustache.corelib.validation.views

import android.content.Context
import android.util.AttributeSet
import android.view.ViewGroup
import dk.mustache.corelib.R
import dk.mustache.corelib.utils.StyleUiHelper
import dk.mustache.corelib.views.OneClickButton

class ValidationButton : OneClickButton, ValidatorView {
    private var currentScreenValidState: Boolean? = null

    constructor(context: Context) : super(context) {
        init(context, null)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context, attrs)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) :
            super(context, attrs, defStyleAttr) {
        init(context, attrs)
    }

    override fun init(context: Context, attrs: AttributeSet?) {
        super.init(context, attrs)
        background = StyleUiHelper.getXmlBackgroundValue(
            context,
            attrs,
            default = R.drawable.validation_edittext_standard_background
        )
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()

        // Listen for validation changes on other validation views in parent and
        // update viewstate based on the new state
        val validationViews = getAllChildValidationViews(parent as ViewGroup)
        listenOnChildValidationViewValidationStates(validationViews, ::updateValidState)
    }

    private fun updateValidState(newValidState: Boolean) {
        if (newValidState != currentScreenValidState) {
            currentScreenValidState = newValidState
            isEnabled = newValidState
        }
    }
}